package com.weskley.roomdb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TodoDialog(
    state: TodoState,
    viewModel: TodoViewModel = hiltViewModel()
) {
    AlertDialog(
        title = {
            Text(text = "Add Todo")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.title,
                    onValueChange = {
                        viewModel.onEvent(TodoEvent.SetTitle(it))
                    },
                    placeholder = {
                        Text(text = "Title")
                    }
                )
                TextField(
                    value = state.text,
                    onValueChange = {
                        viewModel.onEvent(TodoEvent.SetText(it))
                    },
                    placeholder = {
                        Text(text = "Text")
                    }
                )
                TextField(
                    value = state.image,
                    onValueChange = {
                        viewModel.onEvent(TodoEvent.SetImage(it))
                    },
                    placeholder = {
                        Text(text = "Image")
                    }
                )
            }
        },
        onDismissRequest = {
            viewModel.onEvent(TodoEvent.HideDialog)
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.onEvent(TodoEvent.SaveTodo)
            }) {
                Text(text = "Save")
            }
        }
    )
}