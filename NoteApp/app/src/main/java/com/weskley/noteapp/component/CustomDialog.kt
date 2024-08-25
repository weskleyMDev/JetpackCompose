package com.weskley.noteapp.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weskley.noteapp.model.NoteEntity
import com.weskley.noteapp.state.NoteState

@Composable
fun CustomDialog(
    openDialog: Boolean,
    isUpdateMode: Boolean,
    noteToUpdate: NoteEntity?,
    state: NoteState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (openDialog) {
        LaunchedEffect(noteToUpdate) {
            if (isUpdateMode && noteToUpdate != null) {
                state.title.value = noteToUpdate.title
                state.content.value = noteToUpdate.content
            } else {
                state.title.value = ""
                state.content.value = ""
            }
        }
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text(text = if (isUpdateMode) "Update" else "Save")
                }
            },
            text = {
                Column {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.title.value,
                        onValueChange = { newValue -> state.title.value = newValue }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.content.value,
                        onValueChange = { newValue -> state.content.value = newValue }
                    )
                }
            }
        )
    }
}