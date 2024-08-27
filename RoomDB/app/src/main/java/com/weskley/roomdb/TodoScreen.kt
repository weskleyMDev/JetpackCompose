package com.weskley.roomdb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(TodoEvent.ShowSaveDialog)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { paddingValues ->
        if(state.saveDialog) {
            /*LaunchedEffect(Unit) {
                onEvent(TodoEvent.FindTodoById(state.todoList[index].id))
            }*/
            TodoDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.todoList.size) { index ->
                ItemTodo(state = state, onEvent = onEvent, index = index)
            }
        }
    }
}

@Composable
fun ItemTodo(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    index: Int
) {
    if(state.updateDialog) {
        UpdateDialog(state = state, onEvent = onEvent, index = index)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.LightGray)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            Text(text = state.todoList[index].image)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = state.todoList[index].title)
            Text(text = state.todoList[index].text)
        }
        IconButton(onClick = {
            onEvent(TodoEvent.ShowUpdateDialog)
        }) {
            Icon(
                imageVector = Icons.TwoTone.Edit,
                contentDescription = "Update Todo",
                tint = Color.Blue,
                modifier = Modifier.size(30.dp)
            )
        }
        IconButton(onClick = {
            onEvent(TodoEvent.DeleteTodo(state.todoList[index]))
        }) {
            Icon(
                imageVector = Icons.TwoTone.Delete,
                contentDescription = "Delete Todo",
                tint = Color.Red,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}