package com.weskley.hdc_app.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun PercentBar(
    canvasSize: Dp = 300.dp,
    indicatorValue: Short = 0,
    maxIndicatorValue: Short = 100,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    indicatorBackgroundWidth: Float = 100f,
    foregroundColor: Color = MaterialTheme.colorScheme.primary,
    indicatorForegroundWidth: Float = 100f,
    strokeCap: StrokeCap = StrokeCap.Round,
    bigTextSize: TextUnit = MaterialTheme.typography.titleLarge.fontSize,
    bigTextColor: Color = MaterialTheme.colorScheme.onSurface,
    bigTextSuffix: String = "%",
    smallText: String = "ATINGIDO",
    smallTextSize: TextUnit = MaterialTheme.typography.titleSmall.fontSize,
    smallTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
) {
    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue)
    }
    allowedIndicatorValue =
        if (indicatorValue <= maxIndicatorValue) indicatorValue else maxIndicatorValue
    var animatedIndicatorValue by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }
    val percentage = (animatedIndicatorValue / maxIndicatorValue) * 100
    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    val receiveValue by animateIntAsState(
        targetValue = allowedIndicatorValue.toInt(),
        animationSpec = tween(1000), label = ""
    )
    val animatedTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == maxIndicatorValue) bigTextColor else MaterialTheme.colorScheme.onSurface.copy(0.5f),
        animationSpec = tween(1000), label = ""
    )

    val animatedBarColor by animateColorAsState(
        targetValue = when(percentage) {
            in 0f..30f -> Color.Red
            in 30f..80f -> Color.Yellow
            else -> Color.Green
        },
        animationSpec = tween(1000), label = ""
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                val indicatorSize = size / 1.25f
                backgroundIndicator(
                    componentSize = indicatorSize,
                    indicatorColor = backgroundColor,
                    indicatorStrokeWidth = indicatorBackgroundWidth,
                    strokeCap = strokeCap
                )
                foregroundIndicator(
                    indicatorSweepAngle = sweepAngle,
                    componentSize = indicatorSize,
                    indicatorColor = animatedBarColor,
                    indicatorStrokeWidth = indicatorForegroundWidth,
                    strokeCap = strokeCap
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextComponent(
            bigText = percentage.toInt().toShort(),
            bigTextSize = bigTextSize,
            bigTextColor = animatedTextColor,
            bigTextSuffix = bigTextSuffix,
            smallText = smallText,
            smallTextColor = smallTextColor,
            smallTextSize = smallTextSize
        )
    }
}

private fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    strokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = strokeCap
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

private fun DrawScope.foregroundIndicator(
    indicatorSweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    strokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = indicatorSweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = strokeCap
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun TextComponent(
    bigText: Short,
    bigTextSize: TextUnit,
    bigTextColor: Color,
    bigTextSuffix: String,
    smallText: String,
    smallTextColor: Color,
    smallTextSize: TextUnit,
) {
    Text(
        text = smallText,
        color = smallTextColor,
        fontSize = smallTextSize,
        textAlign = TextAlign.Center
    )
    Text(
        text = "$bigText ${bigTextSuffix.take(2)}",
        color = bigTextColor,
        fontSize = bigTextSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}