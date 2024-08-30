package com.weskley.hdc_app.component

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AlarmAdd
import androidx.compose.material.icons.twotone.ArrowDropDown
import androidx.compose.material.icons.twotone.CameraAlt
import androidx.compose.material.icons.twotone.CircleNotifications
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material.icons.twotone.PhotoAlbum
import androidx.compose.material.icons.twotone.WatchLater
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.weskley.hdc_app.R
import com.weskley.hdc_app.state.NotificationState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationDialog(
    showDialog: Boolean,
    medicamentoNome: String,
    medicamentoDose: String,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    state: NotificationState
) {
    val openPicker = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()
    val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val isOn = remember { mutableStateOf(false) }
    val expanded = remember { mutableStateOf(false) }
    val items = listOf("Todos os dias", "Semanal", "Mensal")
    val selectedItem = remember { mutableStateOf(items.first()) }
    val context = LocalContext.current
    val repeticao = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && imageUri.value != null) {
            state.image.value = imageUri.value.toString()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            state.image.value = it.toString()
            Toast.makeText(context, "Imagem selecionada da galeria", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(context, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    fun createImageUri(context: Context): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "HDC_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera")
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    if (showDialog) {
        LaunchedEffect(showDialog) {
            state.title.value = medicamentoNome
            state.body.value = medicamentoDose
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "ALARME", textAlign = TextAlign.Center) },
            icon = {
                Icon(
                    Icons.TwoTone.CircleNotifications,
                    contentDescription = null,
                    modifier = Modifier.size(38.dp)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = state.title.value,
                        onValueChange = { state.title.value = it },
                        label = { Text(text = "Medicamento") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.outline_pill_24),
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        maxLines = 2
                    )
                    OutlinedTextField(
                        value = state.body.value,
                        onValueChange = { state.body.value = it },
                        label = { Text(text = "Descrição") },
                        leadingIcon = {
                            Icon(
                                Icons.TwoTone.Description,
                                contentDescription = null
                            )
                        },
                        singleLine = true,
                        maxLines = 2
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = state.time.value,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(text = "Horário") },
                            leadingIcon = {
                                Icon(
                                    Icons.TwoTone.WatchLater,
                                    contentDescription = null
                                )
                            }
                        )
                        IconButton(onClick = { openPicker.value = true }) {
                            Icon(
                                modifier = Modifier.size(34.dp),
                                imageVector = Icons.TwoTone.AlarmAdd,
                                contentDescription = "Selecionar Horário"
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = state.image.value,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(text = "Imagem") },
                            leadingIcon = {
                                Icon(
                                    Icons.TwoTone.Image,
                                    contentDescription = null
                                )
                            },
                            singleLine = true
                        )
                        IconButton(onClick = {
                            imageUri.value = createImageUri(context)
                            cameraLauncher.launch(imageUri.value!!)
                        }) {
                            Icon(
                                modifier = Modifier.size(34.dp),
                                imageVector = Icons.TwoTone.CameraAlt,
                                contentDescription = "Selecionar Imagem"
                            )
                        }
                        IconButton(onClick = {
                            galleryLauncher.launch("image/jpeg")
                        }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.TwoTone.PhotoAlbum,
                                contentDescription = null
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            modifier = Modifier.weight(1f),
                            expanded = expanded.value,
                            onExpandedChange = { expanded.value = !expanded.value }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(),
                                enabled = isOn.value,
                                value = repeticao.value,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    Icon(
                                        Icons.TwoTone.ArrowDropDown,
                                        contentDescription = null
                                    )
                                },
                                label = { Text(text = "Repetição") }
                            )
                            if (isOn.value) {
                                ExposedDropdownMenu(
                                    expanded = expanded.value,
                                    onDismissRequest = { expanded.value = !expanded.value }
                                ) {
                                    items.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item) },
                                            onClick = {
                                                selectedItem.value = item
                                                repeticao.value = item
                                                expanded.value = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Switch(
                            checked = isOn.value,
                            onCheckedChange = { isOn.value = it }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSave()
                        Toast.makeText(
                            context,
                            "Notificação Salva",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text("SALVAR")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("CANCELAR")
                }
            }
        )

        if (openPicker.value) {
            MyTimePicker(
                onDismiss = { scope.launch { openPicker.value = false } },
                onConfirm = { time ->
                    scope.launch {
                        openPicker.value = false
                        calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                        calendar.set(Calendar.MINUTE, time.minute)
                        state.time.value = formatter.format(calendar.time)
                    }
                }
            )
        }
    }
}

