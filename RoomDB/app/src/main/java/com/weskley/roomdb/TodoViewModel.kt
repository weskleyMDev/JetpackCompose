package com.weskley.roomdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoDao: TodoDao
) : ViewModel() {
//    private val todoDao = MainApplication.todoDataBase.getTodoDao()
//    val todoList = todoDao.getAllTodo()

    val todoList = todoDao.getAllTodo()

    fun addTodo(
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
    }
}