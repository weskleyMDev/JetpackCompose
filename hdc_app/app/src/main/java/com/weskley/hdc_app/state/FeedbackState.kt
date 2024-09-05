package com.weskley.hdc_app.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.weskley.hdc_app.model.Feedback

data class FeedbackState(
    val feedbackList: List<Feedback> = emptyList(),
    val medicine: MutableState<String> = mutableStateOf(""),
    val answer: MutableState<String> = mutableStateOf(""),
    val justification: MutableState<String> = mutableStateOf(""),
    val entryTime: MutableState<String> = mutableStateOf(""),
    val shippingTime: MutableState<String> = mutableStateOf(""),
    val isAddDialogOpen: MutableState<Boolean> = mutableStateOf(true),
    val isUpdateDialogOpen: MutableState<Boolean> = mutableStateOf(false),
    val feedbackUpdated: Feedback? = null
)
