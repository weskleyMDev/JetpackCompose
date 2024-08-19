package com.weskley.hdc_app.component

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.R
import com.weskley.hdc_app.state.NotificationEvent
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val context = LocalContext.current.applicationContext
    val state by viewModel.state.collectAsState()
    val calendar = Calendar.getInstance()
    val sheetState = rememberModalBottomSheetState()
    val formatter = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
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
                    .width(IntrinsicSize.Min),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DropMenu(state = state)
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
                    value = state.body,
                    onValueChange = { newBody ->
                        viewModel.onEvent(NotificationEvent.SetBody(newBody))
                    },
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
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = {
                            viewModel.pickerState()
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_alarm_24),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        viewModel.onEvent(NotificationEvent.ClearTextFields)
                    }) {
                        Text(
                            text = "CANCELAR",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(onClick = {
//                                viewModel.setAlarm()
                        viewModel.onEvent(NotificationEvent.SaveNotification)
                        Toast.makeText(
                            context,
                            state.title,
                            Toast.LENGTH_SHORT
                        ).show()
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
                onDismiss = { viewModel.isPickerOpen = false },
                onConfirm = {
                    viewModel.isPickerOpen = false
                    calendar.set(Calendar.HOUR_OF_DAY, it.hour)
                    calendar.set(Calendar.MINUTE, it.minute)
                    viewModel.hora = it.hour
                    viewModel.minuto = it.minute
                    viewModel.onEvent(
                        NotificationEvent.SetTime(
                            formatter.format(calendar.time)
                        )
                    )
                }
            )
        }
    }
}
