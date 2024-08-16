package com.weskley.hdc_app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.weskley.hdc_app.R
import com.weskley.hdc_app.component.CustomAlert

@Composable
fun Feedback(argument: String) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val openDialog = remember { mutableStateOf(true) }
        val text = remember {
            mutableStateOf("")
        }
        if (openDialog.value) {
            CustomAlert(
                onDismiss = {
                    openDialog.value = false
                },
                onConfirm = {
                    openDialog.value = false
                    text.value = "FEEDBACK ENVIADO COM SUCESSO!"
                },
                title = argument,
                text = "CORPO DO QUESTIONARIO",
                icon = R.drawable.outline_release_alert_24
            )
        }
        Text(text = text.value)
    }
}