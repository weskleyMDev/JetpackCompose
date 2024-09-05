package com.weskley.hdc_app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ChevronLeft
import androidx.compose.material.icons.twotone.ChevronRight
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.CustomAlert
import com.weskley.hdc_app.event.FeedbackEvent
import com.weskley.hdc_app.model.Feedback
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
    val options = listOf("Sim", "Não")
    var isSelected by remember { mutableStateOf(options[0]) }
    val text = remember {
        mutableStateOf("")
    }
    var feedback by remember {
        mutableStateOf("")
    }

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
            title = medicine,
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
    Column(
        modifier = Modifier
            .fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(feedbackState.feedbackList) {item ->
                FeedbackItem(feedback = item)
            }
        }
    }
}

@Composable
fun FeedbackItem(
    feedback: Feedback
) {
    val buttonExpanded = remember { mutableStateOf(false) }
    val textExpanded = remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        onClick = { textExpanded.value = !textExpanded.value },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = feedback.medicine,
                            fontSize = 22.sp
                        )
                        Text(
                            text = feedback.shippingTime
                        )
                    }
                    IconButton(onClick = {
                        buttonExpanded.value = !buttonExpanded.value
                    }) {
                        Icon(
                            imageVector =
                            if (buttonExpanded.value)
                                Icons.TwoTone.ChevronRight
                            else
                                Icons.TwoTone.ChevronLeft,
                            contentDescription = null
                        )
                    }
                    if (buttonExpanded.value) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.TwoTone.Delete,
                                contentDescription = null
                            )
                        }
                    }
                }
            if (textExpanded.value) {
                Text(text = feedback.answer)
                Text(text = feedback.justification)
                Text(text = feedback.entryTime)
            }
        }
    }
}
