package com.weskley.hdc_app.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.R
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current.applicationContext
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DropMenu()
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    placeholder = {
                        Text(
                            text = "Descrição",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    label = {
                        Text(text = "Descrição")
                    },
                    textStyle = MaterialTheme.typography.titleLarge,
                    value = viewModel.descricao.value,
                    onValueChange = { newDesc ->
                        viewModel.changeDesc(newDesc)
                        viewModel.body = newDesc
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Card {
                        Text(
                            text = "Horário: ${
                                viewModel.hora.toString().padStart(2, '0')
                            }:${viewModel.minuto.toString().padStart(2, '0')}",
                            modifier = Modifier
                                .padding(8.dp),
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            viewModel.pickerState()
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_alarm_24),
                            contentDescription = null
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        viewModel.clearFields()
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismiss()
                            }
                        }
                    }) {
                        Text(
                            text = "CANCELAR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        when {
                            viewModel.titulo.value.isEmpty() -> {
                                Toast.makeText(context, "Preencha o Título", Toast.LENGTH_SHORT)
                                    .show()
                                return@TextButton
                            }
                            viewModel.descricao.value.isEmpty() -> {
                                Toast.makeText(context, "Preencha a Descrição", Toast.LENGTH_SHORT)
                                    .show()
                                return@TextButton
                            }
                            else -> {
                                viewModel.setAlarm()
                                Toast.makeText(context, "Alarme Definido!!", Toast.LENGTH_SHORT).show()
                                viewModel.changeTitulo("")
                                viewModel.changeDesc("")
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onDismiss()
                                    }
                                }
                            }
                        }
                    }) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        if (viewModel.isPickerOpen) {
            MyTimePicker(
                onDismiss = { viewModel.isPickerOpen = false },
                onConfirm = {
                    viewModel.isPickerOpen = false
                    viewModel.hora = it.hour
                    viewModel.minuto = it.minute
                }
            )
        }
    }
}
