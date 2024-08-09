package com.weskley.alarmmanagerexample

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun MyAlarm(modifier: Modifier = Modifier) {
    var isOpen by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current.applicationContext
        var hour by remember { mutableIntStateOf(0) }
        var minute by remember { mutableIntStateOf(0) }
        Button(onClick = {
            isOpen = !isOpen
        }) {
            Text(text = "SHOW")
            if (isOpen)
                ShowPicker(
                    hide = { isOpen = false },
                    picker = { state ->
                        isOpen = false
                        hour = state.hour
                        minute = state.minute
                    }
                )
        }
        Text(text = "ALARM: ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")
        Button(onClick = {
            AlarmService().setAlarm(context, hour, minute)
            Toast.makeText(context, "ALARM SET", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "SET ALARM")
        }
        Button(onClick = {
            AlarmService().cancelAlarm(context)
            Toast.makeText(context, "ALARM CANCELLED", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "CANCEL ALARM")
        }
    }
}