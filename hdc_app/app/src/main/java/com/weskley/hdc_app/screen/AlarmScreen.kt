package com.weskley.hdc_app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.BottomSheet
import com.weskley.hdc_app.ui.theme.DarkBlue
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
        contentAlignment = Alignment.TopStart
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
            Row(
                modifier = Modifier
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(DarkBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${ viewModel.hora.toString().padStart(2, '0') }:${ viewModel.minuto.toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {

                    Text(
                        text = viewModel.label,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = viewModel.body
                    )
                }
                Image(
                    painter = painterResource(id = viewModel.myImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}
