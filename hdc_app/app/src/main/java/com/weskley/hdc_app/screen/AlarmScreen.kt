package com.weskley.hdc_app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.AlarmCArd
import com.weskley.hdc_app.component.BottomSheet
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.state.NotificationEvent
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import kotlinx.coroutines.launch

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    if (state.isOpened) {
        BottomSheet(
            onDismiss = { viewModel.onEvent(NotificationEvent.HideBottomSheet) }
        )
    }
    fun onSwitchOn(isChecked: Boolean, item: CustomNotification) {
        viewModel.onEvent(NotificationEvent.SetActive(isChecked, item.id))
        if (isChecked) {
            scope.launch {
                viewModel.setAlarm(
                    item.id,
                    item.title,
                    item.body,
                    item.image,
                    item.time.split(":")[0].toInt(),
                    item.time.split(":")[1].toInt()
                )
            }
        } else {
            scope.launch {
                viewModel.cancelAlarm(item.id)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.notifications) { item ->
                AlarmCArd(item = item, onSwitchOn = {
                    onSwitchOn(it, item)
                },
                    onDelete = {
                        scope.launch {
                            viewModel.cancelAlarm(item.id)
                        }
                        viewModel.onEvent(NotificationEvent.DeleteNotification(item.id))
                    },
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 8.dp, end = 8.dp)
                .align(Alignment.End),
            onClick = {
                viewModel.onEvent(NotificationEvent.ShowBottomSheet)
            },
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}