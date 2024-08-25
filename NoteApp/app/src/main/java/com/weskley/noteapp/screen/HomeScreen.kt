package com.weskley.noteapp.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weskley.noteapp.component.CustomDialog
import com.weskley.noteapp.model.NoteEntity
import com.weskley.noteapp.state.NoteEvent
import com.weskley.noteapp.state.NoteState

@Composable
fun HomeScreen(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
) {
    Log.d("HomeScreen", "Current state: $state")
    val openSave = remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val updatedNote = remember { mutableStateOf<NoteEntity?>(null) }

    CustomDialog(
        openDialog = openSave.value,
        isUpdateMode = isUpdate.value,
        noteToUpdate = updatedNote.value,
        state = state,
        onDismiss = {
            openSave.value = false
            isUpdate.value = false
            updatedNote.value = null
        },
        onConfirm = {
            if (isUpdate.value && updatedNote.value != null) {
                onEvent(
                    NoteEvent.UpdateNote(updatedNote.value!!.copy(
                        title = state.title.value,
                        content = state.content.value
                    ))
                )
            } else {
                onEvent(NoteEvent.SaveNote)
            }
        }
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openSave.value = true
                isUpdate.value = false
                updatedNote.value = null
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(state.noteList) { item ->
                    ItemNote(
                        note = item,
                        onEvent = onEvent,
                        onUpdate = {
                            openSave.value = true
                            isUpdate.value = true
                            updatedNote.value = item
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemNote(
    note: NoteEntity,
    onEvent: (NoteEvent) -> Unit,
    onUpdate: () -> Unit
) {
    val isActive = remember { mutableStateOf(note.isActive) }
    LaunchedEffect(note.isActive) {
        isActive.value = note.isActive
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = note.id.toString(), fontSize = 22.sp)
        Column {
            Text(text = note.title)
            Text(text = note.content)
        }
        Switch(
            checked = isActive.value,
            onCheckedChange = {
                isActive.value = it
                onEvent(NoteEvent.UpdateNoteActiveStatus(note.id, it))
                Log.d("ItemNote", "Note status updated: ${note.id}")
            }
        )
        IconButton(onClick = { onEvent(NoteEvent.DeleteNote(note)) }) {
            Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null)
        }
        IconButton(onClick = {
            onUpdate()
        }) {
            Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
        }
    }
}

