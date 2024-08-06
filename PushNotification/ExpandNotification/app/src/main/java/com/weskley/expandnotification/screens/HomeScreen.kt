package com.weskley.expandnotification.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.expandnotification.MainViewModel

@Composable
fun Home(context: Context, mainViewModel: MainViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier.focusRequester(mainViewModel.focus),
            value = mainViewModel.title,
            onValueChange = {
                mainViewModel.title = it
            },
            placeholder = {
                Text(text = "Title")
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = mainViewModel.message,
            onValueChange = {
                mainViewModel.message = it
            },
            placeholder = {
                Text(text = "Message")
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                when {
                    mainViewModel.title == "" -> Toast.makeText(
                        context,
                        "Insert a Title",
                        Toast.LENGTH_SHORT
                    ).show()
                    mainViewModel.message == "" -> Toast.makeText(
                        context,
                        "Insert a Message",
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> {
                        mainViewModel.showSimpleNotification(context)
                        mainViewModel.message = ""
                        mainViewModel.title = ""
                        mainViewModel.focus.requestFocus()
                    }
                }

            }
        ) {
            Text(text = "Trigger Notification")
        }
    }
}