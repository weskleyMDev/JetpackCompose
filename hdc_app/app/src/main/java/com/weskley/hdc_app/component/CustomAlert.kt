package com.weskley.hdc_app.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Feedback
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomAlert(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: @Composable (() -> Unit)? = null,
    icon: ImageVector? = null,
    confirmText: String = "OK",
//    dismissText: String = "Cancelar"
) {
    AlertDialog(
        icon = {
            Icon(
                icon ?: Icons.TwoTone.Feedback,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "você tomou o medicamento abaixo corretamente?",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
        },
        text = text,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() }
            ) {
                Text(text = confirmText)
            }
        },
        /*dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = dismissText)
            }
        }*/
    )
}
