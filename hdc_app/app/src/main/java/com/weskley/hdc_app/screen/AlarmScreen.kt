package com.weskley.hdc_app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weskley.hdc_app.component.MyTimePicker
import com.weskley.hdc_app.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen() {
    var isOpen by remember {
        mutableStateOf(false)
    }
    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = {
                isOpen = !isOpen
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            if (isOpen) {
                MyTimePicker(
                    onDismiss = { isOpen = false },
                    onConfirm = {
                        isOpen = false
                        hour = it.hour
                        minute = it.minute
                    }
                )
            }
        }
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "ALARME: ${hour.toString().padStart(2, '0')}:${
                    minute.toString().padStart(2, '0')
                }"
            )
        }
    }
}