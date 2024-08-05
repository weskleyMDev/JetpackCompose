package com.weskley.basicnotification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NotificationScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    var text by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = mainViewModel::showNotification) {
            Text(text = "Show")
        }
        TextField(value = text, onValueChange = {
            newValue ->
            text = newValue
        })
        Button(onClick = { mainViewModel.createNotification(
            title = text.trim().replaceFirstChar { it.uppercase() },
            "MUNDO")
        }) {
            Text(text = "Create")
        }
    }
}