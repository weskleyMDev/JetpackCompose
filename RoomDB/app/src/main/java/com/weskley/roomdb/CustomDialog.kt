package com.weskley.roomdb

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    title: String,
    text: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text(text = "SALVAR")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onCancel()
            }) {
                Text(text = "CANCELAR")
            }
        },
        title = { Text(text = title) },
        text = text
    )
}