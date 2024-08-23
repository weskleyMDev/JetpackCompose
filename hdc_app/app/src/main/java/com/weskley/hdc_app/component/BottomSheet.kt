package com.weskley.hdc_app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddAlarm
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.state.NotificationEvent
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()
    val sheetState = rememberModalBottomSheetState()
    val formatter = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }
    val debounceScope = rememberCoroutineScope()
    var textFieldDebounceJob by remember { mutableStateOf<Job?>(null) }

    fun updateBodyText(newBody: String) {
        textFieldDebounceJob?.cancel()
        textFieldDebounceJob = debounceScope.launch {
            delay(300) // Debounce delay
            if (newBody.length <= 100) {
                viewModel.onEvent(NotificationEvent.SetBody(newBody))
            }
        }
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .width(280.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DropMenu(state = state)
                TextField(
                    placeholder = {
                        Text(
                            text = "Descrição",
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    label = {
                        Text(text = "Descrição")
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    value = state.body,
                    onValueChange = { newBody ->
                        updateBodyText(newBody)
                    },
                    minLines = 1,
                    maxLines = 2,
                    supportingText = {
                        Text(
                            text = "${state.body.length} / 100",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        modifier = Modifier.width(120.dp),
                        value = state.time,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = {
                            Text(
                                text = "Horário",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        label = {
                            Text(text = "Horário")
                        },
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            scope.launch { viewModel.pickerState() }
                        }) {
                        Icon(
                            imageVector = Icons.TwoTone.AddAlarm,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        scope.launch { viewModel.onEvent(NotificationEvent.ClearTextFields) }
                    }) {
                        Text(
                            text = "CANCELAR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(onClick = {
                        scope.launch { viewModel.onEvent(NotificationEvent.SaveNotification) }
                    }) {
                        Text(
                            text = "SALVAR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        if (viewModel.isPickerOpen) {
            MyTimePicker(
                onDismiss = { scope.launch(Dispatchers.IO) { viewModel.isPickerOpen = false } },
                onConfirm = {
                    scope.launch {
                        viewModel.isPickerOpen = false
                        calendar.set(Calendar.HOUR_OF_DAY, it.hour)
                        calendar.set(Calendar.MINUTE, it.minute)
                        viewModel.onEvent(
                            NotificationEvent.SetTime(
                                formatter.format(calendar.time)
                            )
                        )
                    }
                }
            )
        }
    }
}
