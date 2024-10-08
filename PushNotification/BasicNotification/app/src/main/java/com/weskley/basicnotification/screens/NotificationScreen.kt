package com.weskley.basicnotification.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.weskley.basicnotification.MainViewModel
import com.weskley.basicnotification.navigation.Screens

@Composable
fun NotificationScreen(
    context: Context = LocalContext.current,
    navController: NavHostController, mainViewModel: MainViewModel =
        hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = mainViewModel::showNotification) {
            Text(text = "Show")
        }
        TextField(value = mainViewModel.text, onValueChange = { newValue ->
            mainViewModel.text = newValue
        })
        Button(onClick = {
            mainViewModel.createNotification(
                context,
                title = mainViewModel.text.trim()
                    .replaceFirstChar { value -> value.uppercase() },
                "MUNDO"
            )
        }) {
            Text(text = "Create")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            navController.navigate(
                route = Screens.Details.passArgument(
                    name = "KOTLIN"
                )
            )
        }) {
            Text(text = "Details")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = mainViewModel::showNotificationProgress) {
            Text(text = "Show Progress")
        }
    }
}