package com.weskley.roomdb

data class TodoState(
    val todoList: List<Todo> = emptyList(),
    val title: String = "",
    val text: String = "",
    val image: String = "",
    val saveDialog: Boolean = false,
    val updateDialog: Boolean = false,
)
