package com.weskley.roomdb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Alarm
import androidx.compose.material.icons.twotone.Keyboard
import androidx.compose.material.icons.twotone.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDialog(
    state: TodoState,
    viewModel: TodoViewModel = hiltViewModel()
) {
    val openDialog = remember {
        mutableStateOf(false)
    }
    val changePicker = remember {
        mutableStateOf(false)
    }
    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )
    val formatter = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }
    if (openDialog.value) {
        AlertDialog(
            title = { Text(text = "SELECIONE UM HORÁRIO")},
            text = {
                if (!changePicker.value)
                    TimePicker(state = timePickerState)
                else
                    TimeInput(state = timePickerState)
            },
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(onClick = {
                    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    calendar.set(Calendar.MINUTE, timePickerState.minute)
                    viewModel.onEvent(TodoEvent.SetImage(formatter.format(calendar.time)))
                    openDialog.value = false
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                IconButton(onClick = {
                    changePicker.value = !changePicker.value
                }) {
                    if (!changePicker.value)
                        Icon(imageVector = Icons.TwoTone.Keyboard, contentDescription = null)
                    else
                        Icon(imageVector = Icons.TwoTone.Alarm, contentDescription = null)
                }
            },
        )
    }
    AlertDialog(
        title = {
            Text(text = "ALARME")
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    textStyle = MaterialTheme.typography.bodyLarge,
                    value = state.title,
                    onValueChange = {
                        viewModel.onEvent(TodoEvent.SetTitle(it))
                    },
                    placeholder = {
                        Text(text = "Título")
                    }
                )
                TextField(
                    textStyle = MaterialTheme.typography.bodyLarge,
                    value = state.text,
                    onValueChange = {
                        viewModel.onEvent(TodoEvent.SetText(it))
                    },
                    placeholder = {
                        Text(text = "Texto")
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier
                            .weight(1f),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        readOnly = true,
                        value = state.image,
                        placeholder = {
                            Text(
                                text = "Hora",
                            )
                        },
                        onValueChange = {}
                    )
                    IconButton(onClick = {
                        openDialog.value = true
                    }) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.TwoTone.NotificationsActive,
                            contentDescription = "Access Alarm"
                        )
                    }
                }
            }
        },
        onDismissRequest = {
            viewModel.onEvent(TodoEvent.HideDialog)
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.onEvent(TodoEvent.SaveTodo)
            }) {
                Text(text = "SALVAR")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.onEvent(TodoEvent.HideDialog)
            }) {
                Text(text = "CANCELAR")
            }
        }
    )
}