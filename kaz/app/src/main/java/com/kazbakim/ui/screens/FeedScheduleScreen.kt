package com.kazbakim.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kazbakim.data.entity.FeedSchedule
import com.kazbakim.ui.theme.*
import com.kazbakim.viewmodel.FeedScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScheduleScreen(viewModel: FeedScheduleViewModel = viewModel()) {
    val feedSchedules by viewModel.feedSchedules.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Yem Vakitleri",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OceanBlue,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = TealAccent,
                contentColor = DarkOcean,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ekle", modifier = Modifier.size(32.dp))
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                SoftWhite,
                                SurfaceBlue
                            )
                        )
                    )
            )
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = feedSchedules,
                    key = { it.id }
                ) { schedule ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it -> it },
                            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(300)),
                        exit = slideOutVertically(
                            targetOffsetY = { it -> -it },
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeOut(animationSpec = tween(300))
                    ) {
                        FeedScheduleCard(
                            schedule = schedule,
                            onToggleAlarm = { viewModel.toggleAlarm(schedule) },
                            onDelete = { viewModel.deleteFeedSchedule(schedule) }
                        )
                    }
                }
                
                if (feedSchedules.isEmpty()) {
                    item {
                        EmptyStateCard(
                            icon = Icons.Default.Restaurant,
                            message = "Henüz yem vakti eklenmedi",
                            submessage = "İlk yem vaktini eklemek için + butonuna tıklayın"
                        )
                    }
                }
            }
        }
    }
    
    if (showAddDialog) {
        AddFeedScheduleDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { time, description ->
                viewModel.addFeedSchedule(time, description)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun FeedScheduleCard(
    schedule: FeedSchedule,
    onToggleAlarm: () -> Unit,
    onDelete: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cardScale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { isPressed = true },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundLight
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(OceanBlue, TealAccent)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        schedule.time,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanBlue
                    )
                    if (schedule.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            schedule.description,
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onToggleAlarm,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (schedule.isAlarmEnabled) 
                                TealAccent.copy(alpha = 0.2f) 
                            else Color.Gray.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        if (schedule.isAlarmEnabled) Icons.Default.Notifications
                        else Icons.Default.NotificationsNone,
                        contentDescription = "Alarm",
                        tint = if (schedule.isAlarmEnabled) TealAccent else TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            ErrorRed.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Sil",
                        tint = ErrorRed,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddFeedScheduleDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var time by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Yeni Yem Vakti Ekle") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Saat (HH:mm)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Açıklama (Opsiyonel)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(time, description) }
            ) {
                Text("Ekle")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}
