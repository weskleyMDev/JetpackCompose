package com.weskley.hdc_app.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.AddAPhoto
import androidx.compose.material.icons.twotone.AddAlarm
import androidx.compose.material.icons.twotone.AddPhotoAlternate
import androidx.compose.material.icons.twotone.CrisisAlert
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.KeyboardDoubleArrowDown
import androidx.compose.material.icons.twotone.NotificationsActive
import androidx.compose.material.icons.twotone.NotificationsOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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
import androidx.navigation.NavController
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.weskley.hdc_app.R
import com.weskley.hdc_app.component.MyTimePicker
import com.weskley.hdc_app.component.ShimmerEffect
import com.weskley.hdc_app.controller.ScreenController
import com.weskley.hdc_app.database.TreatmentDatabase
import com.weskley.hdc_app.event.MedicineEvent
import com.weskley.hdc_app.model.Medicine
import com.weskley.hdc_app.ui.theme.Blue
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.ui.theme.LightBlue
import com.weskley.hdc_app.ui.theme.MediumDarkBlue
import com.weskley.hdc_app.ui.theme.Turquoise
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import com.weskley.hdc_app.viewmodel.MedicineViewModel
import com.weskley.hdc_app.viewmodel.TreatmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    viewModel: MedicineViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current.applicationContext
    val db = Room.databaseBuilder(
        context,
        TreatmentDatabase::class.java,
        TreatmentDatabase.DATABASE_NAME
    ).build()
    val medicineDao = db.medicineDao()
    Log.d("ProfileScreen", "ViewModel instance in ProfileScreen: $viewModel, HashCode: ${System.identityHashCode(viewModel)}")
    val isUpdate = remember { mutableStateOf(false) }
    val updateMedicine = remember {
        mutableStateOf<Medicine?>(null)
    }
    val medicineState by viewModel.state.collectAsState()
    val medicineEvent = viewModel::medicineEvent
    val isLoading = remember { mutableStateOf(true) }
    val showDialog = remember { mutableStateOf(true) }
    val openTimePicker = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val types = listOf("", "Comprimido", "Dosador(ml)", "Gotas")
    val repetition = listOf("", "2", "4", "6", "8", "12", "24")
    val expandedTypes = remember { mutableStateOf(false) }
    val expandedRepetition = remember { mutableStateOf(false) }
    val selectedTextTypes = remember { mutableStateOf(types[0]) }
    val selectedTextRepetition = remember { mutableStateOf(repetition[0]) }
    val showAddDialog = remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
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
    fun incrementAlarm(id: Int, amount: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val medicine = medicineDao.getMedicineById(id) ?: return@launch
            val newCount = medicine.count + amount
            val updatedMedicine = medicine.copy(count = newCount)
            medicineDao.upsertMedicine(updatedMedicine)
        }
    }

    LaunchedEffect(isLoading.value) {
        delay(2000)
        isLoading.value = false
    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(text = "OK")
                }
            },
            text = {
                Text(
                    text = "LEMBRE-SE DE VERIFICAR SE OS ALARMES UTILIZADOS ESTÃO ATIVOS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.TwoTone.CrisisAlert,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        )
    }
    if (openTimePicker.value) {
        MyTimePicker(
            onDismiss = { scope.launch { openTimePicker.value = false } },
            onConfirm = { time ->
                scope.launch {
                    openTimePicker.value = false
                    calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                    calendar.set(Calendar.MINUTE, time.minute)
                    medicineState.time.value = formatter.format(calendar.time)
                }
            }
        )
    }
    if (medicineState.showAddMedicine.value) {
        // Atualize o estado do formulário baseado em isUpdate
        val isUpdating = isUpdate.value && updateMedicine.value != null
        if (isUpdating) {
            medicineState.name.value = updateMedicine.value!!.name
            medicineState.amount.value = updateMedicine.value!!.amount
            medicineState.type.value = updateMedicine.value!!.type
            medicineState.time.value = updateMedicine.value!!.time
            medicineState.image.value = updateMedicine.value!!.image
            medicineState.repetition.value = updateMedicine.value!!.repetition
        } else {
            medicineState.name.value = ""
            medicineState.amount.value = ""
            medicineState.type.value = ""
            medicineState.time.value = ""
            medicineState.image.value = ""
            medicineState.repetition.value = ""
        }

        CustomAddDialog(
            title = {},
            onDismiss = { medicineState.showAddMedicine.value = false },
            onConfirm = { medicineState.showAddMedicine.value = false },
        ) {
            OutlinedTextField(
                value = medicineState.name.value,
                onValueChange = { medicineState.name.value = it },
            )
            OutlinedTextField(
                value = medicineState.amount.value,
                onValueChange = { medicineState.amount.value = it },
            )
            OutlinedTextField(
                value = medicineState.time.value,
                onValueChange = { /*não mudar o valor aqui*/ },
            )
            IconButton(onClick = { openTimePicker.value = true }) {
                Icon(imageVector = Icons.TwoTone.AddAlarm, contentDescription = null)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (medicineState.medicines.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(46.dp),
                    imageVector = Icons.TwoTone.NotificationsOff,
                    contentDescription = null
                )
                Text(
                    text = "Nenhuma Notificação Encontrada",
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
                items(medicineState.medicines) { item ->
                    if (!isLoading.value) {
                        MedicineItem(
                            medicine = item,
                            onEvent = viewModel::medicineEvent,
                            onUpdate = {
                                isUpdate.value = true
                                updateMedicine.value = item
                                medicineEvent(MedicineEvent.UpdateMedicine(updateMedicine.value!!))
                            }
                        )
                    } else {
                        ShimmerEffect()
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 8.dp, end = 8.dp)
                .align(Alignment.End),
            onClick = {
                /*isUpdate.value = false
                updateMedicine.value = null
                medicineEvent(MedicineEvent.ShowAddMedicineDialog)*/
                navController.navigate(ScreenController.Treatment.route)
            },
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}

@Composable
fun CustomAddDialog(
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

@Composable
fun DisplayImage(imagePath: String) {
    val painter = rememberAsyncImagePainter(imagePath)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.Gray),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MedicineItem(
    medicine: Medicine,
    onEvent: (MedicineEvent) -> Unit,
    onUpdate: () -> Unit,
    viewModel: AlarmViewModel = hiltViewModel(),
) {
    val isActive = remember {
        mutableStateOf(medicine.active)
    }
    LaunchedEffect(medicine.active) {
        isActive.value = medicine.active
    }
    var flipped by remember { mutableStateOf(false) }
    var shouldFlip by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "",
    )

    fun onSwitchOn(isChecked: Boolean) {
        if (isChecked) {
            viewModel.setAlarm(medicine)
        } else {
            viewModel.cancelAlarm(medicine.id)
        }
    }

    LaunchedEffect(shouldFlip) {
        if (shouldFlip) {
            flipped = false
            delay(600)
            shouldFlip = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.Black)
            .graphicsLayer {
                rotationX = rotation
                cameraDistance = 8f * density
            }
    ) {
        if (flipped) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Blue, LightBlue)
                        )
                    )
                    .graphicsLayer {
                        if (rotation < 90f) {
                            alpha = 0f
                        }
                        rotationX = 180f
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DisplayImage(imagePath = medicine.image)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        IconButton(
                            onClick = {
                                flipped = !flipped
                            },
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.TopCenter),
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.KeyboardDoubleArrowDown,
                                contentDescription = null,
                                tint = DarkBlue,
                            )
                        }
                        Text(
                            text = "${medicine.amount} - ${medicine.type}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(80.dp),
                    ) {
                        IconButton(
                            enabled = false,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(34.dp),
                            onClick = {
                                onUpdate()
                                shouldFlip = true
                            }) {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp),
                                painter = painterResource(id = R.drawable.twotone_edit_notifications_24),
                                contentDescription = null,
                                tint = Color.Blue
                            )
                        }
                        IconButton(
                            enabled = false,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .size(34.dp),
                            onClick = {
                                viewModel.cancelAlarm(medicine.id)
                                onEvent(MedicineEvent.DeleteMedicine(medicine))
                                shouldFlip = true
                            }) {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp),
                                imageVector = Icons.TwoTone.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(LightBlue, Turquoise)
                        )
                    )
                    .graphicsLayer {
                        if (rotation > 90f) {
                            alpha = 0f
                        }
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(DarkBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = medicine.time,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        IconButton(
                            onClick = {
                                flipped = !flipped
                            },
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.TopCenter)
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.KeyboardDoubleArrowDown,
                                contentDescription = null,
                                tint = DarkBlue,
                            )
                        }
                        Text(
                            text = medicine.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkBlue,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                    Switch(
                        enabled = false,
                        checked = medicine.active,
                        onCheckedChange = {
                            isActive.value = it
                            onEvent(MedicineEvent.UpdateActiveStatus(it, medicine.id))
                            onSwitchOn(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = DarkBlue,
                            checkedThumbColor = MediumDarkBlue,
                            checkedBorderColor = MediumDarkBlue

                        ),
                        thumbContent = if (isActive.value) {
                            {
                                Icon(
                                    modifier = Modifier.size(36.dp),
                                    imageVector = Icons.TwoTone.NotificationsActive,
                                    contentDescription = null,
                                    tint = Color.Green
                                )
                            }
                        } else {
                            {
                                Icon(
                                    modifier = Modifier.size(36.dp),
                                    imageVector = Icons.TwoTone.NotificationsOff,
                                    contentDescription = null,
                                )
                            }
                        }
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