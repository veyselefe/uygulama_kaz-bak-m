package com.kazbakim.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kazbakim.ui.theme.*

@Composable
fun WaveAnimation(
    modifier: Modifier = Modifier,
    waveColor: Color = TealAccent,
    backgroundColor: Color = OceanBlue
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    
    val waveOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave1"
    )
    
    val waveOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave2"
    )
    
    val waveOffset3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave3"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(backgroundColor)
    ) {
        // Wave layers
        WaveLayer(
            offset = waveOffset1,
            color = waveColor.copy(alpha = 0.3f),
            amplitude = 30f,
            frequency = 0.02f,
            yOffset = 50.dp
        )
        
        WaveLayer(
            offset = waveOffset2,
            color = waveColor.copy(alpha = 0.5f),
            amplitude = 40f,
            frequency = 0.015f,
            yOffset = 30.dp
        )
        
        WaveLayer(
            offset = waveOffset3,
            color = waveColor.copy(alpha = 0.7f),
            amplitude = 35f,
            frequency = 0.018f,
            yOffset = 10.dp
        )
    }
}

@Composable
fun WaveLayer(
    offset: Float,
    color: Color,
    amplitude: Float,
    frequency: Float,
    yOffset: androidx.compose.ui.unit.Dp
) {
    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) { canvas, size ->
        val path = androidx.compose.ui.graphics.Path()
        val width = size.width
        val height = size.height
        
        path.moveTo(0f, height)
        
        for (x in 0..width.toInt() step 5) {
            val y = height - yOffset.toPx() - 
                    amplitude * kotlin.math.sin((x + offset) * frequency)
            path.lineTo(x.toFloat(), y)
        }
        
        path.lineTo(width, height)
        path.close()
        
        canvas.drawPath(
            path,
            androidx.compose.ui.graphics.Paint().apply {
                this.color = color
                style = androidx.compose.ui.graphics.Paint.Style.Fill
            }
        )
    }
}

@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(OceanBlue, DeepSea, TealAccent)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    val colorOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = colors.size.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "colorOffset"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = colors,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    )
}

@Composable
fun BubbleAnimation(
    modifier: Modifier = Modifier,
    bubbleColor: Color = Color.White.copy(alpha = 0.3f)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bubbles")
    
    Box(modifier = modifier) {
        repeat(8) { index ->
            val yOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 300f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000 + index * 500, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "bubble_$index"
            )
            
            val xOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 50f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000 + index * 300, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bubble_x_$index"
            )
            
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500 + index * 200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bubble_scale_$index"
            )
            
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .offset(
                        x = (50 + index * 40 + xOffset).dp,
                        y = (-yOffset).dp
                    )
                    .size((10 + index * 2).dp)
                    .background(
                        color = bubbleColor,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}
