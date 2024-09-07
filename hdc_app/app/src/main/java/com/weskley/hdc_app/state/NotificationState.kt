package com.weskley.hdc_app.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.weskley.hdc_app.model.CustomNotification

data class NotificationState(
    val notifications: List<CustomNotification> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val type: MutableState<String> = mutableStateOf(""),
    val time: MutableState<String> = mutableStateOf(""),
    val image: MutableState<String> = mutableStateOf(""),
    val active: MutableState<Boolean> = mutableStateOf(false),
)
