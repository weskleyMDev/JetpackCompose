package com.weskley.hdc_app.event

import com.weskley.hdc_app.model.User

sealed interface UserEvent {
    data object SaveUser: UserEvent
    data object ShowDialog: UserEvent
    data object HideDialog: UserEvent
    data class DeleteUser(val user: User): UserEvent
    data class EditUser(val user: User): UserEvent
}