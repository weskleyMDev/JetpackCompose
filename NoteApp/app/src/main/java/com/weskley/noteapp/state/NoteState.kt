package com.weskley.noteapp.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.weskley.noteapp.model.NoteEntity

data class NoteState(
    val noteList: List<NoteEntity> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val content: MutableState<String> = mutableStateOf(""),
    val time: MutableState<String> = mutableStateOf(""),
    val isActive: MutableState<Boolean> = mutableStateOf(true),
)
