package com.weskley.hdc_app.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AlarmAdd
import androidx.compose.material.icons.twotone.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.weskley.hdc_app.R
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.state.NotificationState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertDialog(
    openDialog: Boolean,
    isUpdate: Boolean,
    notificationUpdate: CustomNotification?,
    state: NotificationState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val openPicker = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val expanded = remember { mutableStateOf(false) }
    val items = listOf(
        "Dipirona" to R.drawable.dipirona,
        "Paracetamol" to R.drawable.paracetamol,
        "Ibuprofeno" to R.drawable.ibuprofeno,
        "Estomazil" to R.drawable.estomazil,
    )
    val selectedItem = remember { mutableStateOf(items.first()) }
    if (openDialog) {
        LaunchedEffect(notificationUpdate) {
            if (isUpdate && notificationUpdate != null) {
                state.title.value = notificationUpdate.title
                state.body.value = notificationUpdate.body
                state.time.value = notificationUpdate.time
                state.image.value = notificationUpdate.image
            } else {
                state.title.value = ""
                state.body.value = ""
                state.time.value = ""
                state.image.value = selectedItem.value.second
            }
        }
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text(text = if (isUpdate) "ATUALIZAR" else "SALVAR")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = "CANCELAR")
                }
            },
            text = {
                Column {
                    TextField(
                        value = selectedItem.value.first,
                        onValueChange = {},
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    expanded.value = true
                                }) {
                                Icon(
                                    imageVector = Icons.TwoTone.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        items.forEach { (itemName, itemIcon) ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = itemName)
                                },
                                onClick = {
                                    selectedItem.value =
                                        items.first { it.first == itemName }
                                    state.image.value = items.first { it.second == itemIcon }.second
                                    state.title.value = items.first { it.first == itemName }.first
                                    expanded.value = false
                                }
                            )
                        }
                    }
                    TextField(
                        value = state.body.value,
                        onValueChange = { state.body.value = it },
                    )
                    Row {
                        TextField(
                            modifier = Modifier.weight(1f),
                            value = state.time.value,
                            onValueChange = {},
                            readOnly = true
                        )
                        IconButton(onClick = {
                            openPicker.value = true
                        }) {
                            Icon(
                                imageVector = Icons.TwoTone.AlarmAdd,
                                contentDescription = "Selecionar Horário"
                            )
                        }
                    }
                }
            }
        )
        if (openPicker.value) {
            MyTimePicker(
                onDismiss = { scope.launch { openPicker.value = false } },
                onConfirm = {
                    scope.launch {
                        openPicker.value = false
                        calendar.set(Calendar.HOUR_OF_DAY, it.hour)
                        calendar.set(Calendar.MINUTE, it.minute)
                        state.time.value = formatter.format(calendar.time)
                    }
                }
            )
        }
    }
}