package com.weskley.hdc_app.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.CrisisAlert
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.KeyboardDoubleArrowDown
import androidx.compose.material.icons.twotone.NotificationsActive
import androidx.compose.material.icons.twotone.NotificationsOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.weskley.hdc_app.R
import com.weskley.hdc_app.component.ShimmerEffect
import com.weskley.hdc_app.component.UpsertDialog
import com.weskley.hdc_app.event.NotificationEvent
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.ui.theme.Blue
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.ui.theme.LightBlue
import com.weskley.hdc_app.ui.theme.MediumDarkBlue
import com.weskley.hdc_app.ui.theme.Turquoise
import com.weskley.hdc_app.viewmodel.AlarmViewModel
import kotlinx.coroutines.delay

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val openDialog = remember { mutableStateOf(false) }
    val isUpdate = remember { mutableStateOf(false) }
    val updateNotification = remember {
        mutableStateOf<CustomNotification?>(null)
    }
    val state by viewModel.state.collectAsState()
    val fieldTitle = remember { mutableStateOf("") }
    val fieldBody = remember { mutableStateOf("") }
    val fieldTime = remember { mutableStateOf("") }
    val fieldImage = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(true) }
    val showDialog = remember { mutableStateOf(true) }

    LaunchedEffect(isLoading.value) {
        delay(2000)
        isLoading.value = false
    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(text = "OK")
                }
            },
            text = {
                Text(
                    text = "LEMBRE-SE DE VERIFICAR SE OS ALARMES UTILIZADOS ESTÃO ATIVOS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.TwoTone.CrisisAlert,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        )
    }
    UpsertDialog(
        openDialog = openDialog.value,
        isUpdate = isUpdate.value,
        notificationUpdate = updateNotification.value,
        state = state,
        onDismiss = {
            openDialog.value = false
            isUpdate.value = false
            updateNotification.value = null
        },
        onConfirm = {
            if (isUpdate.value && updateNotification.value != null) {
                viewModel.onEvent(
                    NotificationEvent.UpdateNotification(
                        updateNotification.value!!.copy(
                            title = fieldTitle.value,
                            type = fieldBody.value,
                            time = fieldTime.value,
                            image = fieldImage.value,
                            active = false
                        )
                    )
                )
                viewModel.cancelAlarm(updateNotification.value!!.id)
            } else {
                viewModel.onEvent(
                    NotificationEvent.SaveNotification
                )
            }
        }
    )
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
                    if (!isLoading.value) {
                        ItemNotification(
                            notification = item,
                            onEvent = viewModel::onEvent,
                            onUpdate = {
                                openDialog.value = true
                                isUpdate.value = true
                                updateNotification.value = item
                            }
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
                openDialog.value = true
                isUpdate.value = false
                updateNotification.value = null
            },
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}

@Composable
fun DisplayImage(imagePath: String) {
    val painter = rememberAsyncImagePainter(imagePath)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.Gray),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ItemNotification(
    notification: CustomNotification,
    onEvent: (NotificationEvent) -> Unit,
    onUpdate: () -> Unit,
    viewModel: AlarmViewModel = hiltViewModel(),
) {
    val isActive = remember {
        mutableStateOf(notification.active)
    }
    LaunchedEffect(notification.active) {
        isActive.value = notification.active
    }
    var flipped by remember { mutableStateOf(false) }
    var shouldFlip by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "",
    )

    fun onSwitchOn(isChecked: Boolean) {
        if (isChecked) {
            viewModel.setAlarm(notification)
        } else {
            viewModel.cancelAlarm(notification.id)
        }
    }

    LaunchedEffect(shouldFlip) {
        if (shouldFlip) {
            flipped = false
            delay(600)
            shouldFlip = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.Black)
            .graphicsLayer {
                rotationX = rotation
                cameraDistance = 8f * density
            }
    ) {
        if (flipped) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Blue, LightBlue)
                        )
                    )
                    .graphicsLayer {
                        if (rotation < 90f) {
                            alpha = 0f
                        }
                        rotationX = 180f
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DisplayImage(imagePath = notification.image)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        IconButton(
                            onClick = {
                                flipped = !flipped
                            },
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.TopCenter),
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.KeyboardDoubleArrowDown,
                                contentDescription = null,
                                tint = DarkBlue,
                            )
                        }
                        Text(
                            text = notification.type,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(80.dp),
                    ) {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(34.dp),
                            onClick = {
                                onUpdate()
                                shouldFlip = true
                            }) {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp),
                                painter = painterResource(id = R.drawable.twotone_edit_notifications_24),
                                contentDescription = null,
                                tint = Color.Blue
                            )
                        }
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .size(34.dp),
                            onClick = {
                                viewModel.cancelAlarm(notification.id)
                                onEvent(NotificationEvent.DeleteNotification(notification))
                                shouldFlip = true
                            }) {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp),
                                imageVector = Icons.TwoTone.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(LightBlue, Turquoise)
                        )
                    )
                    .graphicsLayer {
                        if (rotation > 90f) {
                            alpha = 0f
                        }
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
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
                            text = notification.time,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        IconButton(
                            onClick = {
                                flipped = !flipped
                            },
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.TopCenter)
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.KeyboardDoubleArrowDown,
                                contentDescription = null,
                                tint = DarkBlue,
                            )
                        }
                        Text(
                            text = notification.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = DarkBlue,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )
                    }
                    Switch(
                        checked = isActive.value,
                        onCheckedChange = {
                            isActive.value = it
                            onEvent(NotificationEvent.SetActive(it, notification.id))
                            onSwitchOn(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = DarkBlue,
                            checkedThumbColor = MediumDarkBlue,
                            checkedBorderColor = MediumDarkBlue

                        ),
                        thumbContent = if (isActive.value) {
                            {
                                Icon(
                                    modifier = Modifier.size(36.dp),
                                    imageVector = Icons.TwoTone.NotificationsActive,
                                    contentDescription = null,
                                    tint = Color.Green
                                )
                            }
                        } else {
                            {
                                Icon(
                                    modifier = Modifier.size(36.dp),
                                    imageVector = Icons.TwoTone.NotificationsOff,
                                    contentDescription = null,
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}