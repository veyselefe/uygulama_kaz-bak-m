package com.kazbakim.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.kazbakim.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Nöbet Vakitleri",
            description = "Kaz bakım nöbetlerinizi kolayca yönetin. Kişilere göre nöbet saatleri ve tarihleri belirleyin.",
            icon = Icons.Default.Event
        ),
        OnboardingPage(
            title = "Nöbet Sırası",
            description = "Nöbetçi sırasını oluşturun ve düzenleyin. Sıralamayı kolayca değiştirin.",
            icon = Icons.Default.People
        ),
        OnboardingPage(
            title = "Yem Vakitleri",
            description = "Kazlara yem verme zamanlarını ayarlayın. Otomatik alarm ile asla kaçırmayın.",
            icon = Icons.Default.Restaurant
        ),
        OnboardingPage(
            title = "Su Vakitleri",
            description = "Su değiştirme zamanlarını belirleyin. Kazlarınızın suyu her zaman taze olsun.",
            icon = Icons.Default.WaterDrop
        )
    )
    
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()
    
    // Animation for page transitions
    val infiniteTransition = rememberInfiniteTransition(label = "onboarding")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        OceanBlue,
                        DeepSea
                    )
                )
            )
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingPageContent(
                page = pages[page],
                scale = scale,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Page indicators
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            activeColor = TealAccent,
            inactiveColor = Color.White.copy(alpha = 0.3f),
            indicatorWidth = 12.dp,
            indicatorHeight = 12.dp,
            spacing = 8.dp
        )
        
        // Navigation buttons
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pagerState.currentPage > 0) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Geri",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(80.dp))
            }
            
            if (pagerState.currentPage < pages.size - 1) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealAccent,
                        contentColor = DarkOcean
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .width(120.dp)
                ) {
                    Text(
                        "İleri",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = onOnboardingComplete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealAccent,
                        contentColor = DarkOcean
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .width(120.dp)
                ) {
                    Text(
                        "Başla",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    scale: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "icon")
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconScale"
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
    
    Column(
        modifier = modifier
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        
        // Animated icon
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(iconScale),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        color = TealAccent.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            )
            
            Icon(
                imageVector = page.icon,
                contentDescription = page.title,
                tint = TealAccent,
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale)
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Title
        Text(
            text = page.title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Description
        Text(
            text = page.description,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Decorative elements
        repeat(5) { index ->
            val offset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 20f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000 + index * 400, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "bubble_$index"
            )
            
            Box(
                modifier = Modifier
                    .offset(
                        x = ((index - 2) * 40).dp,
                        y = (-100 - offset).dp
                    )
                    .size(12.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )
        }
    }
}
