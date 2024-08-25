package com.weskley.noteapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weskley.noteapp.model.NoteEntity
import com.weskley.noteapp.service.NoteDao
import com.weskley.noteapp.state.NoteEvent
import com.weskley.noteapp.state.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val databaseDao: NoteDao
) : ViewModel() {

    private val _noteList =
        databaseDao.findAllNotes().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val _state = MutableStateFlow(NoteState())
    val state = combine(_state, _noteList) { state, noteList ->
        state.copy(noteList = noteList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    databaseDao.deleteNote(event.note)
                }
            }

            NoteEvent.SaveNote -> {
                val note = NoteEntity(
                    title = state.value.title.value,
                    content = state.value.content.value,
                    time = state.value.time.value,
                    isActive = state.value.isActive.value
                )
                viewModelScope.launch(Dispatchers.IO) {
                    databaseDao.upsertNote(note)
                    Log.d("NoteViewModel", "Note saved: ${note.id}")
                }
                _state.update { clear ->
                    clear.copy(
                        title = mutableStateOf(""),
                        content = mutableStateOf(""),
                        time = mutableStateOf("")
                    )
                }
            }

            is NoteEvent.UpdateNote -> {
                viewModelScope.launch {
                    val note = event.note.copy(
                        title = state.value.title.value,
                        content = state.value.content.value,
                        time = state.value.time.value,
                        isActive = state.value.isActive.value
                    )
                    viewModelScope.launch(Dispatchers.IO) {
                        databaseDao.upsertNote(note)
                    }
                    _state.update { clear ->
                        clear.copy(
                            title = mutableStateOf(""),
                            content = mutableStateOf(""),
                            time = mutableStateOf("")
                        )
                    }
                    Log.d("NoteViewModel", "Note updated: ${note.id}")
                }
            }

            is NoteEvent.GetNoteById -> {
                viewModelScope.launch {
                    databaseDao.getNoteById(event.id).collect { item ->
                        if (item != null) {
                            Log.d("NoteViewModel", "Note found: ${item.id}")

                            _state.update {
                                it.copy(
                                    title = mutableStateOf(item.title),
                                    content = mutableStateOf(item.content),
                                    time = mutableStateOf(item.time),
                                    isActive = mutableStateOf(item.isActive)
                                )
                            }
                        } else {
                            Log.e("NoteViewModel", "Item with ID ${event.id} not found.")
                        }
                    }
                }
            }

            is NoteEvent.UpdateNoteActiveStatus -> {
                viewModelScope.launch(Dispatchers.IO) {
                    databaseDao.updateNoteActiveStatus(event.id, event.isActive)
                }
            }
        }
    }
}