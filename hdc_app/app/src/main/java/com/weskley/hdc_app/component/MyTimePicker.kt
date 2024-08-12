package com.weskley.hdc_app.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.weskley.hdc_app.R
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val pickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )
    var showDial by remember {
        mutableStateOf(true)
    }
    val toggleIcon = if (showDial) R.drawable.outline_keyboard_24 else R.drawable.outline_av_timer_24

    DialogPicker(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(pickerState) },
        toggle = {
            IconButton(onClick = { showDial = !showDial }) {
                Icon(painter = painterResource(id = toggleIcon),
                    contentDescription = null)
            }
        }
    ) {
        if (showDial) {
            TimePicker(state = pickerState)
        } else {
            TimeInput(state = pickerState)
        }
    }
}