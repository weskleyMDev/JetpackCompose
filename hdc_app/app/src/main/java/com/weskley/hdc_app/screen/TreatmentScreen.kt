package com.weskley.hdc_app.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.AddAPhoto
import androidx.compose.material.icons.twotone.AddAlarm
import androidx.compose.material.icons.twotone.AddPhotoAlternate
import androidx.compose.material.icons.twotone.ArrowDropDown
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Healing
import androidx.compose.material.icons.twotone.PostAdd
import androidx.compose.material.icons.twotone.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.R
import com.weskley.hdc_app.component.MyTimePicker
import com.weskley.hdc_app.component.PercentBar
import com.weskley.hdc_app.component.ShimmerEffectTreatment
import com.weskley.hdc_app.event.MedicineEvent
import com.weskley.hdc_app.event.TreatmentEvent
import com.weskley.hdc_app.model.Medicine
import com.weskley.hdc_app.model.Treatment
import com.weskley.hdc_app.state.MedicineState
import com.weskley.hdc_app.state.TreatmentState
import com.weskley.hdc_app.ui.theme.Blue
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.ui.theme.LightBlue
import com.weskley.hdc_app.ui.theme.color1
import com.weskley.hdc_app.ui.theme.color2
import com.weskley.hdc_app.ui.theme.color3
import com.weskley.hdc_app.ui.theme.color4
import com.weskley.hdc_app.ui.theme.color5
import com.weskley.hdc_app.ui.theme.color6
import com.weskley.hdc_app.ui.theme.color7
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import com.weskley.hdc_app.viewmodel.MedicineViewModel
import com.weskley.hdc_app.viewmodel.TreatmentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowStatus(
    treatmentEvent: (TreatmentEvent) -> Unit,
    treatmentState: TreatmentState,
    medicineEvent: (MedicineEvent) -> Unit,
    medicineState: MedicineState,
    treatment: Treatment?,
    alarmViewModel: AlarmViewModel = hiltViewModel()
) {
    val context = LocalContext.current.applicationContext
    val openPicker = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val types = listOf("", "Comprimido", "Dosador(ml)", "Gotas")
    val repetition = listOf("", "2", "4", "6", "8", "12", "24")
    val expandedTypes = remember { mutableStateOf(false) }
    val expandedRepetition = remember { mutableStateOf(false) }
    val selectedTextTypes = remember { mutableStateOf(types[0]) }
    val selectedTextRepetition = remember { mutableStateOf(repetition[0]) }
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
    var imageUri: Uri? = null
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            medicineState.image.value = imageUri.toString()
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val imagePath = copyImageToAppDirectory(context, it)
            medicineState.image.value = imagePath
            Toast.makeText(context, "Imagem selecionada da galeria", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(context, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    fun createImageUri(context: Context): Uri {
        val file = File(context.filesDir, "HDC_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
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
                selectedTextTypes.value = types[0]
                selectedTextRepetition.value = repetition[0]
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
                    modifier = Modifier.width(130.dp),
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
                ExposedDropdownMenuBox(
                    expanded = expandedTypes.value,
                    onExpandedChange = { expandedTypes.value = !expandedTypes.value }
                ) {
                    OutlinedTextField(
                        value = selectedTextTypes.value,
                        onValueChange = {},
                        label = { Text(text = "Tipo") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTypes.value)
                        },
                        readOnly = true,
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedTypes.value,
                        onDismissRequest = { expandedTypes.value = false }
                    ) {
                        types.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(text = type) },
                                onClick = {
                                    selectedTextTypes.value = type
                                    expandedTypes.value = false
                                    medicineState.type.value = type
                                }
                            )
                        }
                    }
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = medicineState.time.value,
                onValueChange = { medicineState.time.value = it },
                label = { Text(text = "Hora") },
                trailingIcon = {
                    IconButton(onClick = { openPicker.value = true }) {
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            imageUri = createImageUri(context)
                            cameraLauncher.launch(imageUri!!)
                        }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.TwoTone.AddAPhoto, contentDescription = null
                            )
                        }
                        IconButton(onClick = { galleryLauncher.launch("image/jpeg") }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.TwoTone.AddPhotoAlternate,
                                contentDescription = null
                            )
                        }
                    }
                },
                readOnly = true,
                singleLine = true
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                ExposedDropdownMenuBox(
                    expanded = expandedRepetition.value,
                    onExpandedChange = { expandedRepetition.value = !expandedRepetition.value }
                ) {
                    OutlinedTextField(
                        value = selectedTextRepetition.value,
                        onValueChange = {},
                        label = { Text(text = "Repetição(Horas)") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRepetition.value)
                        },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedRepetition.value,
                        onDismissRequest = { expandedRepetition.value = false }
                    ) {
                        repetition.forEach { repetition ->
                            DropdownMenuItem(
                                text = { Text(text = repetition) },
                                onClick = {
                                    selectedTextRepetition.value = repetition
                                    expandedRepetition.value = false
                                    medicineState.repetition.value = repetition
                                }
                            )
                        }
                    }
                }
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
                TextButton(onClick = {
                    medicineEvent(MedicineEvent.ShowAddMedicineDialog)
                }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.TwoTone.Add, contentDescription = null
                        )
                        Text(text = "NOVO", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        ) {
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(color3, color4)
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(28.dp))
                    PercentBar(
                        indicatorValue = indicatorValue.toShort(),
                        maxIndicatorValue = maxIndicatorValue.toShort(),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(250.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredMedicines) { medicine ->
                    val meta = ((treatment?.duration ?: 0) * medicine.amount.toIntOrNull()!!)
                    MedicineItem(
                        medicine = medicine,
                        meta = meta,
                        onUpdate = {},
                        onDelete = {medicineEvent(MedicineEvent.DeleteMedicine(medicine))},
                        event = medicineEvent
                    )
                }
            }
        }
    }
    if (openPicker.value) {
        val calendar = Calendar.getInstance()
        val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
        MyTimePicker(
            onDismiss = { scope.launch { openPicker.value = false } },
            onConfirm = { time ->
                scope.launch {
                    openPicker.value = false
                    calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                    calendar.set(Calendar.MINUTE, time.minute)
                    medicineState.time.value = formatter.format(calendar.time)
                }
            }
        )
    }
}

@Composable
fun MedicineItem(
    medicine: Medicine,
    meta: Int,
    onUpdate: () -> Unit,
    onDelete: () -> Unit,
    event: (MedicineEvent) -> Unit,
    viewModel: AlarmViewModel = hiltViewModel(),
) {
    val isActive = remember { mutableStateOf(medicine.active) }
    val buttonExpanded = remember { mutableStateOf(false) }
    val rotationState = animateFloatAsState(
        targetValue = if (buttonExpanded.value) 180f else 0f, label = ""
    )
    LaunchedEffect(medicine.active) {
        isActive.value = medicine.active
    }
    fun onSwitchOn(isChecked: Boolean) {
        if (isChecked) {
            viewModel.setAlarm(medicine)
        } else {
            viewModel.cancelAlarm(medicine.id)
        }
    }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(color1, color2)
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Switch(
                    checked = isActive.value,
                    onCheckedChange = {
                        event(MedicineEvent.UpdateActiveStatus(it, medicine.id))
                        onSwitchOn(it)
                    }
                )
                Text(text = medicine.name)
                Text(text = "- ${medicine.count} / $meta")
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.rotate(rotationState.value),
                    onClick = { buttonExpanded.value = !buttonExpanded.value }
                ) {
                    Icon(
                        painterResource(R.drawable.twotone_arrow_left_24),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
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
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = MaterialTheme.shapes.extraLarge
                )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.background(
                    Brush.horizontalGradient(
                        listOf(
                            color5,
                            color6,
                            color7
                        )
                    )
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

private fun copyImageToAppDirectory(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri) ?: return ""
    val fileName = "HDC_${System.currentTimeMillis()}.jpg"
    val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
    inputStream.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return context.getFileStreamPath(fileName).absolutePath
}
