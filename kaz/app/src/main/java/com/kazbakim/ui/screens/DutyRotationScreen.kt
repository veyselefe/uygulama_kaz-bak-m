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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kazbakim.data.entity.DutyRotation
import com.kazbakim.ui.theme.*
import com.kazbakim.viewmodel.DutyRotationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DutyRotationScreen(viewModel: DutyRotationViewModel = viewModel()) {
    val dutyRotations by viewModel.dutyRotations.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Nöbet Sırası",
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
                    items = dutyRotations,
                    key = { it.id }
                ) { rotation ->
                    val index = dutyRotations.indexOf(rotation)
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
                        DutyRotationCard(
                            rotation = rotation,
                            index = index,
                            onMoveUp = { viewModel.moveUp(rotation) },
                            onMoveDown = { viewModel.moveDown(rotation) },
                            onDelete = { viewModel.deleteRotation(rotation) }
                        )
                    }
                }
                
                if (dutyRotations.isEmpty()) {
                    item {
                        EmptyStateCard(
                            icon = Icons.Default.People,
                            message = "Henüz nöbetçi eklenmedi",
                            submessage = "İlk nöbetçiyi eklemek için + butonuna tıklayın"
                        )
                    }
                }
            }
        }
    }
    
    if (showAddDialog) {
        AddPersonDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { personName ->
                viewModel.addPersonToRotation(personName)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun DutyRotationCard(
    rotation: DutyRotation,
    index: Int,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
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
                        "${index + 1}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    rotation.personName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanBlue
                )
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onMoveUp,
                    enabled = index > 0,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (index > 0) 
                                TealAccent.copy(alpha = 0.2f) 
                            else Color.Gray.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "Yukarı",
                        tint = if (index > 0) TealAccent else TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(
                    onClick = onMoveDown,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            TealAccent.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Aşağı",
                        tint = TealAccent,
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
fun AddPersonDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var personName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Yeni Kişi Ekle",
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
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(personName) },
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
