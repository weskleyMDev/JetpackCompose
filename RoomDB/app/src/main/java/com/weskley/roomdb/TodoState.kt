package com.weskley.roomdb

data class TodoState(
    val todoList: List<Todo> = emptyList(),
    val title: String = "",
    val text: String = "",
    val image: String = "",
    val isDialogOpen: Boolean = false,
)
