package com.weskley.hdc_app.state

import com.weskley.hdc_app.model.CustomNotification

data class NotificationState(
    val notifications: List<CustomNotification> = emptyList(),
    val title: String = "",
    val body: String = "",
    val time: String = "",
    val image: Int = 0,
    val isOpened: Boolean = false
)
