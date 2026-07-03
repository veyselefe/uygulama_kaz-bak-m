package com.kazbakim.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kazbakim.data.entity.DutySchedule
import com.kazbakim.ui.theme.*
import com.kazbakim.viewmodel.DutyScheduleViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DutyScheduleScreen(viewModel: DutyScheduleViewModel = viewModel()) {
    val dutySchedules by viewModel.dutySchedules.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Nöbet Vakitleri",
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
            // Background gradient
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
                    items = dutySchedules,
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
                        DutyScheduleCard(
                            schedule = schedule,
                            onToggleAlarm = { viewModel.toggleAlarm(schedule) },
                            onDelete = { viewModel.deleteDutySchedule(schedule) }
                        )
                    }
                }
                
                if (dutySchedules.isEmpty()) {
                    item {
                        EmptyStateCard(
                            icon = Icons.Default.Event,
                            message = "Henüz nöbet vakiti eklenmedi",
                            submessage = "İlk nöbet vaktini eklemek için + butonuna tıklayın"
                        )
                    }
                }
            }
        }
    }
    
    if (showAddDialog) {
        AddDutyScheduleDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { personName, startTime, endTime, date ->
                viewModel.addDutySchedule(personName, startTime, endTime, date)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun DutyScheduleCard(
    schedule: DutySchedule,
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
                    Text(
                        schedule.personName.first().toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        schedule.personName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "${schedule.startTime} - ${schedule.endTime}",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        schedule.date,
                        fontSize = 14.sp,
                        color = TextTertiary
                    )
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
fun EmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    message: String,
    submessage: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "emptyState")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        TealAccent.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = TealAccent,
                    modifier = Modifier.size(60.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                message,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OceanBlue,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                submessage,
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun AddDutyScheduleDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String) -> Unit
) {
    var personName by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Yeni Nöbet Vakti Ekle",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ) 
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = personName,
                    onValueChange = { personName = it },
                    label = { Text("Kişi Adı") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = TealAccent,
                        unfocusedBorderColor = TextSecondary
                    )
                )
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Başlangıç Saati (HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = TealAccent,
                        unfocusedBorderColor = TextSecondary
                    )
                )
                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("Bitiş Saati (HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = TealAccent,
                        unfocusedBorderColor = TextSecondary
                    )
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Tarih (yyyy-MM-dd)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = TealAccent,
                        unfocusedBorderColor = TextSecondary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(personName, startTime, endTime, date) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealAccent,
                    contentColor = DarkOcean
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text("Ekle", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextSecondary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text("İptal", fontSize = 16.sp)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}
