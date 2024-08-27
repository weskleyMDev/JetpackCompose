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

    private val _todoList =
        todoDao.findAllTodo().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(TodoState())
    val state = combine(_state, _todoList) { state, todoList ->
        state.copy(
            todoList = todoList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoState())

    fun onEvent(event: TodoEvent) {
        when (event) {
            is TodoEvent.DeleteTodo -> {
                try {
                    viewModelScope.launch(Dispatchers.IO) {
                        todoDao.deleteTodo(event.todo)
                    }
                } catch (e: Exception) {
                    throw Exception("Error deleting todo with id=${event.todo.id}: $e")
                }
            }

            is TodoEvent.SetImage -> {
                try {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                image = event.image
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error setting image: $e")
                }
            }

            is TodoEvent.SetText -> {
                try {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                text = event.text
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error setting text: $e")
                }
            }

            is TodoEvent.SetTitle -> {
                try {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                title = event.title
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error setting title: $e")
                }
            }

            TodoEvent.ShowSaveDialog -> {
                try {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                saveDialog = true
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error showing save dialog: $e")
                }
            }

            TodoEvent.HideSaveDialog -> {
                try {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                saveDialog = false
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error hiding save dialog: $e")
                }
            }

            TodoEvent.ShowUpdateDialog -> {
                try {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                updateDialog = true
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error showing update dialog: $e")
                }
            }

            TodoEvent.HideUpdateDialog -> {
                try {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                updateDialog = false
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error hiding update dialog: $e")
                }
            }

            TodoEvent.SaveTodo -> {
                try {
                    viewModelScope.launch {
                        val title = state.value.title
                        val text = state.value.text
                        val image = state.value.image

                        if (title.isBlank() || text.isBlank() || image.isBlank()) {
                            return@launch
                        }
                        val todo = Todo(
                            title = title,
                            text = text,
                            image = image
                        )
                        viewModelScope.launch(Dispatchers.IO) {
                            todoDao.upsertTodo(todo)
                        }
                        _state.update {
                            it.copy(
                                title = "",
                                text = "",
                                image = "",
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error saving todo: $e")
                }
            }

            is TodoEvent.UpdateTodo -> {
                viewModelScope.launch {
                    try {
                        state.value.todoList.find { it.id == event.id }?.let { current ->
                            val updatedTodo = current.copy(
                                title = state.value.title,
                                text = state.value.text,
                                image = state.value.image
                            )
                            viewModelScope.launch(Dispatchers.IO) {
                                todoDao.upsertTodo(updatedTodo)
                            }
                            _state.update {
                                it.copy(
                                    title = "",
                                    text = "",
                                    image = "",
                                )
                            }
                        }
                    } catch (e: Exception) {
                        throw Exception("Error updating todo with id=${event.id}: $e")
                    }
                }
            }

            is TodoEvent.FindTodoById -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        todoDao.findTodoById(event.id).collect { todo ->
                            _state.update {
                                it.copy(
                                    title = todo.title,
                                    text = todo.text,
                                    image = todo.image
                                )
                            }
                        }
                    } catch (e: Exception) {
                        throw Exception("Error finding todo by id=${event.id}: $e")
                    }
                }
            }

            TodoEvent.ClearFields -> {
                try {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                title = "",
                                text = "",
                                image = "",
                            )
                        }
                    }
                } catch (e: Exception) {
                    throw Exception("Error clearing fields: $e")
                }
            }
        }
    }
}