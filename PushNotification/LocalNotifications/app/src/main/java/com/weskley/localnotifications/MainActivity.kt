package com.weskley.localnotifications

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.weskley.localnotifications.ui.theme.LocalNotificationsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalNotificationsTheme {
                val postNotificationPermissionState = rememberPermissionState(
                    permission = android.Manifest.permission.POST_NOTIFICATIONS
                )
                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermissionState.status.isGranted) {
                        postNotificationPermissionState.launchPermissionRequest()
                    }
                }
                Scaffold(modifier = Modifier.fillMaxSize()) {

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                        contentAlignment = Alignment.Center
                    ) {
                        ShowNotification()
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ShowNotification() {
    val context = LocalContext.current.applicationContext
    val notificationService = NotificationService(context)

    Column {
        Button(onClick = { notificationService.showSimpleNotification(
            "HELLO",
            "WORLD"
        ) }) {
            Text(text = "Show Notification")
        }
    }
}
