package com.weskley.hdc_app.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun CustomAlert(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: @Composable () -> Unit,
    text: @Composable (() -> Unit)? = null,
    icon: Int
) {
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Red
            )
        },
        title = title,
        text = text,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = "Cancelar")
            }
        }
    )
}
