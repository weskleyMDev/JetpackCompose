package com.weskley.hdc_app.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.BottomSheet
import com.weskley.hdc_app.viewmodel.AlarmViewModel

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    var isOpen by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = {
                isOpen = true
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            if (isOpen) {
                BottomSheet(
                    onDismiss = { isOpen = false }
                )
            }
        }
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .height(IntrinsicSize.Min),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TITULO: ${viewModel.titulo}"
                )
                Text(
                    text = "DESCRICAO: ${viewModel.descricao}"
                )
                Text(
                    text = "ALARME: ${
                        viewModel.hora.toString().padStart(2, '0')
                    }:${viewModel.minuto.toString().padStart(2, '0')}"
                )
            }
        }
    }
}
