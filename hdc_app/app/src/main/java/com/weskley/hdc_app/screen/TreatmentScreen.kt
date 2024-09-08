package com.weskley.hdc_app.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.AddAPhoto
import androidx.compose.material.icons.twotone.AddAlarm
import androidx.compose.material.icons.twotone.ArrowDropDown
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.R
import com.weskley.hdc_app.component.PercentBar
import com.weskley.hdc_app.component.ShimmerEffectTreatment
import com.weskley.hdc_app.event.MedicineEvent
import com.weskley.hdc_app.event.TreatmentEvent
import com.weskley.hdc_app.model.Medicine
import com.weskley.hdc_app.model.Treatment
import com.weskley.hdc_app.state.MedicineState
import com.weskley.hdc_app.state.TreatmentState
import com.weskley.hdc_app.viewmodel.MedicineViewModel
import com.weskley.hdc_app.viewmodel.TreatmentViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TreatmentScreen(
    treatmentViewModel: TreatmentViewModel = hiltViewModel(),
    medicineViewModel: MedicineViewModel = hiltViewModel()
) {
    val treatmentState by treatmentViewModel.treatmentState.collectAsState()
    val treatmentEvent = treatmentViewModel::treatmentEvent
    val medicineState by medicineViewModel.state.collectAsState()
    val medicineEvent = medicineViewModel::medicineEvent
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val isLoadings = remember { mutableStateOf(true) }
    val isUpdate = remember { mutableStateOf(true) }
    val updatedTreatment = remember { mutableStateOf<Treatment?>(null) }
    LaunchedEffect(Unit) {
        delay(2000)
        isLoadings.value = false
    }
    fun calculateDuration() {
        val startDate = selectedDate.value.format(formatter)
        val endDate = try {
            val daysToAdd = (treatmentState.duration.value.toLongOrNull() ?: 0) - 1
            val endDate = selectedDate.value.plusDays(daysToAdd)
            endDate.format(formatter)
        } catch (e: Exception) {
            selectedDate.value.format(formatter)
        }
        treatmentState.startDate.value = startDate
        treatmentState.endDate.value = endDate
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            if (isUpdate.value && updatedTreatment.value != null) {
                treatmentState.title.value = updatedTreatment.value!!.title
                treatmentState.startDate.value = updatedTreatment.value!!.startDate
                treatmentState.endDate.value = updatedTreatment.value!!.endDate
                treatmentState.duration.value = updatedTreatment.value!!.duration.toString()
            } else {
                treatmentState.title.value = ""
                treatmentState.startDate.value = ""
                treatmentState.endDate.value = ""
                treatmentState.duration.value = ""
            }
            AlertDialog(
                onDismissRequest = {
                    isUpdate.value = false
                    updatedTreatment.value = null
                    treatmentEvent(TreatmentEvent.hideAddDialog)
                },
                confirmButton = {
                    TextButton(onClick = {
                        calculateDuration()
                        treatmentEvent(TreatmentEvent.SaveTreatment)
                        treatmentEvent(TreatmentEvent.hideAddDialog)
                        selectedDate.value = LocalDate.now()
                    }) {
                        Text(text = if (!isUpdate.value) "Salvar" else "Atualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        isUpdate.value = false
                        updatedTreatment.value = null
                        treatmentEvent(TreatmentEvent.hideAddDialog)
                        selectedDate.value = LocalDate.now()
                    }) {
                        Text(text = "Cancelar")
                    }
                },
                title = {
                    Text(
                        text =
                        if (!isUpdate.value)
                            "Adicionar Tratamento"
                        else
                            "Atualizar Tratamento"
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value =
                            if (!isUpdate.value)
                                selectedDate.value.format(formatter)
                            else
                                treatmentState.startDate.value,
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
                            value = treatmentState.duration.value,
                            onValueChange = { newDuration ->
                                treatmentState.duration.value = newDuration
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
                    if (!isLoadings.value) {
                        TreatmentItem(
                            treatment = item,
                            onDelete = {
                                isUpdate.value = false
                                updatedTreatment.value = null
                                treatmentEvent(TreatmentEvent.DeleteTreatment(item))
                            },
                            onUpdate = {
                                isUpdate.value = true
                                updatedTreatment.value = item
                                treatmentEvent(TreatmentEvent.UpdateTreatment(updatedTreatment.value!!))
                            },
                            loadItem = {
                                treatmentEvent(TreatmentEvent.showStatusDialog)
                                updatedTreatment.value = it
                            },
                        )
                    } else {
                        ShimmerEffectTreatment()
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 8.dp, end = 8.dp)
                .align(Alignment.End),
            onClick = {
                isUpdate.value = false
                updatedTreatment.value = null
                treatmentEvent(TreatmentEvent.showAddDialog)
            }) {
            Icon(
                imageVector = Icons.TwoTone.PostAdd,
                contentDescription = null
            )
        }
        if (treatmentState.statusDialog.value) {
            ShowStatus(
                treatmentEvent,
                treatmentState,
                medicineEvent,
                medicineState,
                updatedTreatment.value
            )
        }
    }
}

@Composable
fun ShowStatus(
    treatmentEvent: (TreatmentEvent) -> Unit,
    treatmentState: TreatmentState,
    medicineEvent: (MedicineEvent) -> Unit,
    medicineState: MedicineState,
    treatment: Treatment?
) {
    val filteredMedicines = medicineState.medicines.filter { it.treatmentId == treatment?.id }
    val indicatorValue: Int
    val maxIndicatorValue: Int
    if (filteredMedicines.isEmpty()) {
        indicatorValue = 0
        maxIndicatorValue = 1
    } else {
        indicatorValue = filteredMedicines.sumOf { it.count }
        val totalAmount = filteredMedicines.sumOf { it.amount.toIntOrNull() ?: 0 }
        maxIndicatorValue = (treatment?.duration ?: 0) * totalAmount
    }

    if (medicineState.showAddMedicine.value) {
        CustomDialog(
            title = {
                Text(
                    "Adicionar Medicamento",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            onDismiss = { medicineEvent(MedicineEvent.HideAddMedicineDialog) },
            onConfirm = {
                medicineEvent(MedicineEvent.SetTreatmentId(treatment?.id ?: 0))
                medicineEvent(MedicineEvent.SaveMedicine)
                medicineEvent(MedicineEvent.HideAddMedicineDialog)
            }
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = medicineState.name.value,
                onValueChange = { medicineState.name.value = it },
                label = { Text(text = "Medicamento") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    modifier = Modifier.width(100.dp),
                    value = medicineState.amount.value,
                    onValueChange = { medicineState.amount.value = it },
                    label = {
                        Text(
                            text = "Quantidade",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = medicineState.type.value,
                    onValueChange = { medicineState.type.value = it },
                    label = { Text(text = "Tipo") },
                    trailingIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.TwoTone.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    },
                    readOnly = true
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = medicineState.time.value,
                onValueChange = { medicineState.time.value = it },
                label = { Text(text = "Hora") },
                trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.TwoTone.AddAlarm, contentDescription = null
                        )
                    }
                },
                readOnly = true
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = medicineState.image.value,
                onValueChange = { medicineState.image.value = it },
                label = { Text(text = "Imagem") },
                trailingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.TwoTone.AddAPhoto, contentDescription = null
                        )
                    }
                },
                readOnly = true
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = medicineState.repetition.value,
                    onValueChange = { medicineState.repetition.value = it },
                    label = { Text(text = "Repetição") },
                    trailingIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.TwoTone.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    },
                    readOnly = true
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(checked = false, onCheckedChange = {})
            }
        }
    }
    if (treatmentState.statusDialog.value) {
        CustomDialog(
            title = {
                Column(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${treatment?.title?.uppercase()}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${treatment?.startDate?.uppercase()?.replace("-", "/")} ~ ${
                            treatment?.endDate?.uppercase()?.replace("-", "/")
                        }"
                    )
                }
            },
            onDismiss = { treatmentEvent(TreatmentEvent.hideStatusDialog) },
            onConfirm = { treatmentEvent(TreatmentEvent.hideStatusDialog) },
            toggle = {
                IconButton(onClick = {
                    medicineEvent(MedicineEvent.ShowAddMedicineDialog)
                }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.TwoTone.Add, contentDescription = null
                    )
                }
            }
        ) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            PercentBar(
                indicatorValue = indicatorValue.toShort(),
                maxIndicatorValue = maxIndicatorValue.toShort(),
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(150.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredMedicines) { medicine ->
                    val meta = ((treatment?.duration ?: 0) * medicine.amount.toIntOrNull()!!)
                    MedicineItem(medicine = medicine, medicineEvent = medicineEvent, meta = meta)
                }
            }
        }
    }
}

@Composable
fun MedicineItem(
    medicine: Medicine,
    medicineEvent: (MedicineEvent) -> Unit,
    meta: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        /*IconButton(onClick = {
            medicineEvent(MedicineEvent.IncrementCount(medicine.id, medicine.amount.toIntOrNull() ?: 0))
        }) {
            Icon(imageVector = Icons.TwoTone.Add, contentDescription = null)
        }
        */Text(text = medicine.name, modifier = Modifier.weight(1f))
        Text(text = "${medicine.count} / $meta")
    }
}

@Composable
fun CustomDialog(
    title: @Composable () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.extraLarge
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title()
                content()
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text("CANCELAR") }
                    TextButton(onClick = onConfirm) { Text("OK") }
                }
            }
        }
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
    treatment: Treatment,
    onUpdate: () -> Unit,
    onDelete: () -> Unit,
    loadItem: (Treatment) -> Unit,
) {
    val buttonExpanded = remember { mutableStateOf(false) }
    val rotationState = animateFloatAsState(
        targetValue = if (buttonExpanded.value) 180f else 0f, label = ""
    )
    ElevatedCard(onClick = {
        loadItem(treatment)
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
                    text = treatment.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Data de início: ${treatment.startDate}")
                Text(text = "Data de fim: ${treatment.endDate}")
            }
            IconButton(
                modifier = Modifier.rotate(rotationState.value),
                onClick = { buttonExpanded.value = !buttonExpanded.value }
            ) {
                Icon(
                    painterResource(R.drawable.twotone_arrow_left_24), contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(28.dp)
                )
            }
            if (buttonExpanded.value) {
                IconButton(onClick = {
                    onUpdate()
                    buttonExpanded.value = !buttonExpanded.value
                }) {
                    Icon(
                        imageVector = Icons.TwoTone.Edit, contentDescription = null,
                        tint = Color.Blue,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = {
                    onDelete()
                    buttonExpanded.value = !buttonExpanded.value
                }) {
                    Icon(
                        imageVector = Icons.TwoTone.Delete, contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
