package com.weskley.hdc_app.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEffect() {
    fun Modifier.shimmerEffect(): Modifier = composed {
        val colors = listOf(
            Color.Black.copy(alpha = 0.6f),
            Color.Black.copy(alpha = 0.2f),
            Color.Black.copy(alpha = 0.6f),
        )
        val transition = rememberInfiniteTransition(label = "shimmer")
        val shimmerAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing)),
            label = "shimmer"
        )
        background(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset.Zero,
                end = Offset(x = shimmerAnimation.value, y = shimmerAnimation.value * 2)
            )
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .shimmerEffect()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .height(80.dp)
        ) {
            /*Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            ) {
                Column(
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    Box(modifier = Modifier
                        .height(20.dp)
                        .width(150.dp)
                        .shimmerEffect())
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier
                        .height(15.dp)
                        .width(80.dp)
                        .shimmerEffect())
                }
            }*/
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmerEffectPreview() {
    ShimmerEffect()
}