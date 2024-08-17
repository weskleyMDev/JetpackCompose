package com.weskley.roomdb

sealed interface ContactEvent {
    data object SaveContact: ContactEvent
    data class SetName(val name: String): ContactEvent
    data class SetPhone(val phone: String): ContactEvent
    data class SetEmail(val email: String): ContactEvent
    data object ShowDialog: ContactEvent
    data object HideDialog: ContactEvent
    data class SortContacts(val sortType: SortType): ContactEvent
    data class DeleteContact(val contact: Contact): ContactEvent
}