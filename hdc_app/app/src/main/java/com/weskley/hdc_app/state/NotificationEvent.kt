package com.weskley.hdc_app.state

sealed interface NotificationEvent {
    data object SaveNotification: NotificationEvent
    data object ShowBottomSheet: NotificationEvent
    data object HideBottomSheet: NotificationEvent
    data object ClearTextFields: NotificationEvent
    data object ShowAlert: NotificationEvent

    data class SetTitle(val title: String): NotificationEvent
    data class SetBody(val body: String): NotificationEvent
    data class SetTime(val time: String): NotificationEvent
    data class SetImage(val image: Int): NotificationEvent
    data class SetActive(val active: Boolean, val id: Int): NotificationEvent
    data class DeleteNotification(val id: Int): NotificationEvent
}