package com.weskley.hdc_app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.twotone.Cancel
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weskley.hdc_app.component.BottomSheet
import com.weskley.hdc_app.state.NotificationEvent
import com.weskley.hdc_app.ui.theme.DarkBlue
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
                fun onSwitchOn(isChecked: Boolean) {
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(DarkBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.time,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f),
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = DarkBlue,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = item.body,
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkBlue,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Image(
                            painter = painterResource(id = item.image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                        Column {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        viewModel.cancelAlarm(item.id)
                                    }
                                    viewModel.onEvent(NotificationEvent.DeleteNotification(item.id))
                                }) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    imageVector = Icons.TwoTone.Cancel,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                            Switch(
                                checked = item.active,
                                onCheckedChange = {
                                    onSwitchOn(it)
                                },
                                thumbContent = if (item.active) {
                                    {
                                        Icon(
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            tint = Color.Green
                                        )
                                    }
                                } else {
                                    null
                                },
                            )
                        }
                    }
                }
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