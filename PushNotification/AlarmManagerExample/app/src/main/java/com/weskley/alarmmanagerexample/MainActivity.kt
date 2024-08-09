package com.weskley.alarmmanagerexample

import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.weskley.alarmmanagerexample.ui.theme.AlarmManagerExampleTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmManagerExampleTheme {
                val postNotificationPermissionState = rememberPermissionState(
                    permission = android.Manifest.permission.POST_NOTIFICATIONS
                )
                LaunchedEffect(key1 = true) {
                    if (!postNotificationPermissionState.status.isGranted) {
                        postNotificationPermissionState.launchPermissionRequest()
                    }
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        val navController = rememberNavController()
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPicker(
    hide: () -> Unit,
    picker: (TimePickerState) -> Unit
) {
    val calendar = Calendar.getInstance()
    val state = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )
    DialogPicker(
        onCancel = { hide() },
        onConfirm = { picker(state) }
    ) {
        TimeInput(state = state)
    }
}

@Composable
fun DialogPicker(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "OK")
            }
        },
        text = { content() },
        title = { Text(text = "Select time") },
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlarmManagerExampleTheme {

    }
}