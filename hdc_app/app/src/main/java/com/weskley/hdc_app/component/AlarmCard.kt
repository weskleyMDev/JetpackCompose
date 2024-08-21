package com.weskley.hdc_app.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.twotone.ArrowDropDown
import androidx.compose.material.icons.twotone.Delete
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.weskley.hdc_app.model.CustomNotification
import com.weskley.hdc_app.ui.theme.DarkBlue


@Composable
fun AlarmCArd(
    item: CustomNotification,
    onSwitchOn: (Boolean) -> Unit,
    onDelete: () -> Unit,
) {
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
            .background(Color.LightGray)
            .graphicsLayer {
                rotationX = rotation
                cameraDistance = 8f * density
            }
    ) {
        //BACK
        if (flipped) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.LightGray)
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
                    Text(
                        text = item.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f),
                    )
                    Column {
                        IconButton(onClick = { flipped = !flipped }) {
                            Icon(
                                imageVector = Icons.TwoTone.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = DarkBlue
                            )
                        }
                        IconButton(onClick = {
                            onDelete()
                            flipped = !flipped
                        }) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                imageVector = Icons.TwoTone.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        } else {
            //FRONT
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.LightGray)
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
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Column {
                        IconButton(onClick = { flipped = !flipped }) {
                            Icon(
                                imageVector = Icons.TwoTone.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = DarkBlue
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
}