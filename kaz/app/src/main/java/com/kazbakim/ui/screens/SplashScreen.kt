package com.kazbakim.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kazbakim.ui.theme.*

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    // Animation for splash screen duration
    val scaleAnimation = remember {
        Animatable(0f)
    }
    
    val alphaAnimation = remember {
        Animatable(0f)
    }
    
    LaunchedEffect(Unit) {
        scaleAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        alphaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
        // Wait for splash screen duration
        kotlinx.coroutines.delay(2500)
        onSplashComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        OceanBlue,
                        DeepSea,
                        DarkOcean
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Whale animation (using Lottie)
        WhaleAnimation(
            modifier = Modifier
                .scale(scaleAnimation.value)
                .alpha(alphaAnimation.value)
        )
        
        // App name with animation
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 200.dp)
                .scale(scaleAnimation.value)
                .alpha(alphaAnimation.value)
        ) {
            Text(
                text = "Küçük Balina",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )
            Text(
                text = "Kaz Bakım Asistanı",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = TealAccent,
                letterSpacing = 4.sp
            )
        }
        
        // Loading indicator
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .size(48.dp),
            color = TealAccent,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun WhaleAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.whale_animation)
    )
    
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(200.dp),
        speed = 0.8f
    )
}

// Fallback whale animation using Compose shapes if Lottie is not available
@Composable
fun WhaleAnimationFallback(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "whale")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    
    Box(
        modifier = modifier
            .size(150.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // Simple whale shape using circles
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = TealAccent,
                    shape = CircleShape
                )
                .scale(scale)
        )
        
        // Tail
        Box(
            modifier = Modifier
                .offset(x = (-60).dp, y = 20.dp)
                .size(40.dp)
                .background(
                    color = TealAccent,
                    shape = CircleShape
                )
                .scale(scale * 0.8f)
        )
        
        // Eye
        Box(
            modifier = Modifier
                .offset(x = 20.dp, y = (-10).dp)
                .size(15.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
        )
        
        // Water droplets
        repeat(3) { index ->
            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 30f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500 + index * 300, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "droplet_$index"
            )
            
            Box(
                modifier = Modifier
                    .offset(x = (30 + index * 20).dp, y = (-50 - offset).dp)
                    .size(8.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.6f),
                        shape = CircleShape
                    )
            )
        }
    }
}
