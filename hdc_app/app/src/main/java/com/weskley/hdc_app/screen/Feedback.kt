package com.weskley.hdc_app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.CustomAlert
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Feedback(
    argument: String,
    viewModel: AlarmViewModel = hiltViewModel()
) {
    var timeNow by remember {
        mutableStateOf("")
    }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val openDialog = remember { mutableStateOf(true) }
    val options = listOf("Sim", "Não")
    var isSelected by remember { mutableStateOf(options[0]) }
    val text = remember {
        mutableStateOf("")
    }
    var feedback by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (openDialog.value) {
            CustomAlert(
                onDismiss = {
                    openDialog.value = false
                },
                onConfirm = {
                    val now = LocalDateTime.now()
                    openDialog.value = false
                    text.value = if (isSelected == "Não") feedback else "-"
                    timeNow = now.format(formatter)
                },
                title = argument ,
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        options.forEach { opt ->
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = opt == isSelected,
                                    onClick = { isSelected = opt }
                                )
                                Text(text = opt)
                            }
                        }
                        OutlinedTextField(
                            enabled = isSelected == "Não",
                            value = feedback,
                            onValueChange = {
                                if (it.length <= 100) feedback = it
                            },
                            placeholder = {
                                Text(text = "Digite o motivo...")
                            },
                            label = {
                                Text(text = "Motivo")
                            },
                            maxLines = 3,
                            supportingText = {
                                Text(
                                    text = "${feedback.length} / 100",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                        )
                    }
                },
                confirmText = "Enviar"
            )
        }
        Text(text = "Medicamento: $argument")
        Text(text = "Resposta: $isSelected")
        Text(text = "Feedback: ${text.value}")
        Text(text = "Horário: $timeNow")
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackPreview() {
    Feedback("teste")
}