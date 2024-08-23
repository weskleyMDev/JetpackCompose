package com.weskley.hdc_app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.NotificationsOff
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.AlarmCArd
import com.weskley.hdc_app.component.BottomSheet
import com.weskley.hdc_app.component.ShimmerEffect
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.state.NotificationEvent
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val context = LocalContext.current.applicationContext
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Dispatchers.IO) {
        delay(2000)
        isLoading = false
    }
    if (state.isOpened) {
        BottomSheet(
            onDismiss = { viewModel.onEvent(NotificationEvent.HideBottomSheet) }
        )
    }
    fun onSwitchOn(isChecked: Boolean, item: CustomNotification) {
        scope.launch(Dispatchers.IO) {
            viewModel.onEvent(NotificationEvent.SetActive(isChecked, item.id))
        }
        if (isChecked) {
            viewModel.setAlarm(
                item.id,
                item.title,
                item.body,
                item.image,
                item.time.split(":")[0].toInt(),
                item.time.split(":")[1].toInt()
            )
        } else {
            viewModel.cancelAlarm(item.id)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.notifications.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(46.dp),
                    imageVector = Icons.TwoTone.NotificationsOff,
                    contentDescription = null
                )
                Text(
                    text = "Nenhuma Notificação Encontrada",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.notifications) { item ->
                    if (!isLoading) {
                        AlarmCArd(
                            item = item,
                            onSwitchOn = {
                                onSwitchOn(it, item)
                            },
                            onDelete = {
                                viewModel.cancelAlarm(item.id)
                                viewModel.onEvent(
                                    NotificationEvent.DeleteNotification(
                                        item.id
                                    )
                                )
                            },
                        )
                    } else {
                        ShimmerEffect()
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 8.dp, end = 8.dp)
                .align(Alignment.End),
            onClick = {
                scope.launch(Dispatchers.IO) {
                    viewModel.onEvent(NotificationEvent.ShowBottomSheet)
                }
            },
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}