package com.weskley.roomdb

data class ContactState(
    val contacts: List<Contact> = emptyList(),
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val isAddingContact: Boolean = false,
    val sortType: SortType = SortType.NAME
)
