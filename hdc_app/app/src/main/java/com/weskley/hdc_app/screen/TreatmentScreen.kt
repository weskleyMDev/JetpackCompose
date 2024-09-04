package com.weskley.hdc_app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Healing
import androidx.compose.material.icons.twotone.PostAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.PercentBar
import com.weskley.hdc_app.event.TreatmentEvent
import com.weskley.hdc_app.model.Treatment
import com.weskley.hdc_app.state.TreatmentState
import com.weskley.hdc_app.viewmodel.TreatmentViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TreatmentScreen(
    treatmentViewModel: TreatmentViewModel = hiltViewModel()
) {
    val treatmentState by treatmentViewModel.treatmentState.collectAsState()
    val treatmentEvent = treatmentViewModel::treatmentEvent
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val duration = remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        fun calculateDuration() {
            val startDate = selectedDate.value.format(formatter)
            val endDate = try {
                val daysToAdd = (duration.value.toLongOrNull() ?: 0) - 1
                val endDate = selectedDate.value.plusDays(daysToAdd)
                endDate.format(formatter)
            } catch (e: Exception) {
                selectedDate.value.format(formatter)
            }
            treatmentState.startDate.value = startDate
            treatmentState.endDate.value = endDate
        }
        if (showDatePicker.value) {
            DatePickerModal(
                onDateSelected = {
                    selectedDate.value = it
                    showDatePicker.value = false
                },
                onDismiss = { showDatePicker.value = false }
            )
        }
        if (treatmentState.addDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    treatmentEvent(TreatmentEvent.hideAddDialog)
                },
                confirmButton = {
                    TextButton(onClick = {
                        calculateDuration()
                        treatmentEvent(TreatmentEvent.SaveTreatment)
                        treatmentEvent(TreatmentEvent.hideAddDialog)
                        duration.value = ""
                        selectedDate.value = LocalDate.now()
                    }) {
                        Text(text = "Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        treatmentEvent(TreatmentEvent.hideAddDialog)
                        duration.value = ""
                        selectedDate.value = LocalDate.now()
                    }) {
                        Text(text = "Cancelar")
                    }
                },
                title = {
                    Text(text = "Adicionar Tratamento")
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = selectedDate.value.format(formatter),
                            onValueChange = {},
                            label = {
                                Text(text = "Data Inicial")
                            },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker.value = true }) {
                                    Icon(
                                        imageVector = Icons.TwoTone.CalendarMonth,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        OutlinedTextField(value = treatmentState.title.value,
                            onValueChange = { newTitle ->
                                treatmentState.title.value = newTitle
                            },
                            label = {
                                Text(text = "Título")
                            }
                        )
                        OutlinedTextField(
                            value = duration.value,
                            onValueChange = { newDuration ->
                                duration.value = newDuration
                            },
                            label = {
                                Text(text = "Duração (em dias)")
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            )
        }
        if (treatmentState.treatmentList.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(46.dp),
                    imageVector = Icons.TwoTone.Healing,
                    contentDescription = null
                )
                Text(
                    text = "Nenhum Tratamento Encontrado",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(treatmentState.treatmentList) { item ->
                    ShowStatus(item, treatmentEvent, treatmentState)
                    TreatmentItem(
                        item = item,
                        onEvent = treatmentEvent
                    )
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 8.dp, end = 8.dp)
                .align(Alignment.End),
            onClick = {
                treatmentEvent(TreatmentEvent.showAddDialog)
            }) {
            Icon(
                imageVector = Icons.TwoTone.PostAdd,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ShowStatus(
    item: Treatment,
    treatmentEvent: (TreatmentEvent) -> Unit,
    treatmentState: TreatmentState
) {
    val p = remember { mutableStateOf(6) }
    val d = remember { mutableStateOf(6) }
    val p1 = remember { mutableStateOf(7) }
    val d1 = remember { mutableStateOf(7) }
    val count = remember { mutableStateOf(
        p.value + d.value
    ) }
    val total = remember { mutableStateOf(
        p1.value + d1.value
    ) }
    if (treatmentState.statusDialog.value) {
        AlertDialog(
            onDismissRequest = { treatmentEvent(TreatmentEvent.hideStatusDialog) },
            confirmButton = {
                TextButton(onClick = { treatmentEvent(TreatmentEvent.hideStatusDialog) }) {
                    Text(text = "OK")
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PercentBar(
                        indicatorValue = count.value.toShort(),
                        maxIndicatorValue = total.value.toShort()
                    )
                    HorizontalDivider()
                    Text(text = item.title.replaceFirstChar { it.uppercase() }, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "PARACETAMOL:")
                        Text(text = "${p.value} / ${p1.value}")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "DIPIRONA:")
                        Text(text = "${d.value} / ${d1.value}")
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val selectedDate = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000L))
                    onDateSelected(selectedDate)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TreatmentItem(
    item: Treatment,
    onEvent: (TreatmentEvent) -> Unit
) {
    ElevatedCard(onClick = {
        onEvent(TreatmentEvent.showStatusDialog)
    }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Data de início: ${item.startDate}")
                Text(text = "Data de fim: ${item.endDate}")
            }
            IconButton(onClick = { onEvent(TreatmentEvent.DeleteTreatment(item)) }) {
                Icon(
                    imageVector = Icons.TwoTone.Delete, contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TreatmentScreenPreview() {
    TreatmentScreen()
}