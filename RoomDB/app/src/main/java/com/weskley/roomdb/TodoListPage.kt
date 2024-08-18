package com.weskley.roomdb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TodoListPage(paddingValues: PaddingValues, viewModel: TodoViewModel = hiltViewModel()){

//    val todoList by viewModel.todoList.observeAsState()
    var inputTitle by remember {
        mutableStateOf("")
    }
    var inputText by remember {
        mutableStateOf("")
    }
    var inputImage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = paddingValues.calculateTopPadding())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = inputTitle,
                onValueChange = {
                    inputTitle = it
                })
            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                })
            OutlinedTextField(
                value = inputImage,
                onValueChange = {
                    inputImage = it
                })
            /*Button(onClick = {
                viewModel.addTodo(
                    title = inputTitle,
                    text = inputText,
                    image = inputImage
                )
                inputText = ""
            }) {
                Text(text = "Add")
            }*/
        }

        /*todoList?.let { list ->
            LazyColumn(
                content = {
                    items(list){item: Todo ->
                        TodoItem(item = item, onDelete = {
                            viewModel.deleteTodo(item)
                        })
                    }
                }
            )
        }?: Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "No items yet",
            fontSize = 16.sp
        )*/
    }
}

@Composable
fun TodoItem(item : Todo,onDelete : ()-> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = item.text,
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = item.image,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.twotone_delete_24),
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}