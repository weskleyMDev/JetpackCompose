package com.weskley.roomdb

sealed interface TodoEvent {
    data object SaveTodo : TodoEvent
    data class DeleteTodo(val todo: Todo) : TodoEvent
    data class UpdateTodo(val id: Int) : TodoEvent
    data class FindTodoById(val id: Int) : TodoEvent

    data object ShowSaveDialog : TodoEvent
    data object HideSaveDialog : TodoEvent
    data object ShowUpdateDialog : TodoEvent
    data object HideUpdateDialog : TodoEvent
    data object ClearFields: TodoEvent

    data class SetTitle(val title: String) : TodoEvent
    data class SetText(val text: String) : TodoEvent
    data class SetImage(val image: String) : TodoEvent

}