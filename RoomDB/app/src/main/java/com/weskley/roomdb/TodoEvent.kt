package com.weskley.roomdb

sealed interface TodoEvent {
    data object SaveTodo : TodoEvent
    data object ShowDialog : TodoEvent
    data object HideDialog : TodoEvent
    data class SetTitle(val title: String) : TodoEvent
    data class SetText(val text: String) : TodoEvent
    data class SetImage(val image: String) : TodoEvent
    data class DeleteTodo(val todo: Todo) : TodoEvent

}