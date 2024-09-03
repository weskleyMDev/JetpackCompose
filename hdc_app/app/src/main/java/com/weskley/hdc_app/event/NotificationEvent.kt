package com.weskley.hdc_app.event

import com.weskley.hdc_app.model.CustomNotification

sealed interface NotificationEvent {

    data object ClearTextFields: NotificationEvent

    data object SaveNotification : NotificationEvent

    data class UpdateNotification(val notification: CustomNotification) :
        NotificationEvent

    data class FindNotificationById(val id: Int): NotificationEvent

    data class SetActive(val active: Boolean, val id: Int): NotificationEvent

    data class DeleteNotification(val notification: CustomNotification): NotificationEvent

}