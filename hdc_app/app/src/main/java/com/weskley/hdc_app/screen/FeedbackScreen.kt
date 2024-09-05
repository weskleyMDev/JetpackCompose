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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.CustomAlert
import com.weskley.hdc_app.event.FeedbackEvent
import com.weskley.hdc_app.viewmodel.FeedbackViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun FeedbackScreen(
    medicine: String,
    time: String,
    feedbackViewModel: FeedbackViewModel = hiltViewModel()
) {
    val feedbackState by feedbackViewModel.feedbackState.collectAsState()
    val feedbackEvent = feedbackViewModel::feedBackEvent
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
        fun fetchData() {
            val now = LocalDateTime.now().format(formatter)
            feedbackState.shippingTime.value = now
            feedbackState.medicine.value = medicine
            text.value = if (isSelected == "Não") feedback else "-"
            feedbackState.justification.value = text.value
            feedbackState.answer.value = isSelected
            feedbackState.entryTime.value = time
        }
        if (feedbackState.isAddDialogOpen.value) {
            CustomAlert(
                onDismiss = {
                    feedbackEvent(FeedbackEvent.HideAddFeedback)
                },
                onConfirm = {
                    fetchData()
                    feedbackEvent(FeedbackEvent.SaveFeedback)
                    feedbackEvent(FeedbackEvent.HideAddFeedback)
                },
                title = medicine ,
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
    }
}
