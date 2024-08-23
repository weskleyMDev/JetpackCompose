package com.weskley.hdc_app.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.KeyboardDoubleArrowDown
import androidx.compose.material.icons.twotone.NotificationsActive
import androidx.compose.material.icons.twotone.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.weskley.hdc_app.R
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.ui.theme.Blue
import com.weskley.hdc_app.ui.theme.DarkBlue
import com.weskley.hdc_app.ui.theme.LightBlue
import com.weskley.hdc_app.ui.theme.MediumDarkBlue
import com.weskley.hdc_app.ui.theme.Turquoise


@Composable
fun AlarmCArd(
    item: CustomNotification,
    onSwitchOn: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "",
    )
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
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                    ) {
                        IconButton(
                            onClick = { flipped = !flipped },
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
                            text = item.body,
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
//                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(34.dp),
                            onClick = {
                                onEdit()
                                flipped = !flipped
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
                                onDelete()
                                flipped = !flipped
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
                            text = item.time,
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
                            onClick = { flipped = !flipped },
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
                            text = item.title,
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
                        checked = item.active,
                        onCheckedChange = {
                            onSwitchOn(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = DarkBlue,
                            checkedThumbColor = MediumDarkBlue,
                            checkedBorderColor = MediumDarkBlue

                        ),
                        thumbContent = if (item.active) {
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
                        },
                    )
                }
            }
        }
    }
}
