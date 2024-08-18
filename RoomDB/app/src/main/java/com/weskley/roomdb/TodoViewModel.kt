package com.weskley.roomdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    @DatabaseDao private val todoDao: TodoDao
) : ViewModel() {
//    private val todoDao = MainApplication.todoDataBase.getTodoDao()
//    val todoList = todoDao.getAllTodo()

    private val _todoList = todoDao.getAllTodo().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(TodoState())
    val state = combine(_state, _todoList) { state, todoList ->
        state.copy(
            todoList = todoList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoState())

    fun onEvent(event: TodoEvent) {
        when(event) {
            is TodoEvent.DeleteTodo -> {
                viewModelScope.launch(Dispatchers.IO) {
                    todoDao.deleteTodo(event.todo)
                }
            }
            is TodoEvent.SetImage -> {
                _state.update {
                    it.copy(
                        image = event.image
                    )
                }
            }
            is TodoEvent.SetText -> {
                _state.update {
                    it.copy(
                        text = event.text
                    )
                }
            }
            is TodoEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
            TodoEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isDialogOpen = true
                    )
                }
            }
            TodoEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isDialogOpen = false
                    )
                }
            }
            TodoEvent.SaveTodo -> {
                val title = state.value.title
                val text = state.value.text
                val image = state.value.image

                if (title.isBlank() || text.isBlank() || image.isBlank()) {
                    return
                }
                val todo = Todo(
                    title = title,
                    text = text,
                    image = image
                )
                viewModelScope.launch(Dispatchers.IO) {
                    todoDao.addTodo(todo)
                }
                _state.update {
                    it.copy(
                        title = "",
                        text = "",
                        image = "",
                        isDialogOpen = false
                    )
                }
            }
        }
    }
    /*fun addTodo(
        title: String,
        text: String,
        image: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val elem = Todo(
                title = title,
                text = text,
                image = image
            )
            todoDao.addTodo(elem)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodo(todo)
        }
    }*/
}