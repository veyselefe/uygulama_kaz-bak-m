package com.kazbakim.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kazbakim.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var darkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Ayarlar",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OceanBlue,
                    titleContentColor = Color.White
                )
            )
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
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // User Profile Section
                UserProfileSection()
                
                // Appearance Section
                AppearanceSection(
                    darkMode = darkMode,
                    onDarkModeChange = { darkMode = it }
                )
                
                // Notifications Section
                NotificationsSection(
                    notificationsEnabled = notificationsEnabled,
                    onNotificationsChange = { notificationsEnabled = it },
                    soundEnabled = soundEnabled,
                    onSoundChange = { soundEnabled = it },
                    vibrationEnabled = vibrationEnabled,
                    onVibrationChange = { vibrationEnabled = it }
                )
                
                // Data Section
                DataSection()
                
                // About Section
                AboutSection()
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun UserProfileSection() {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundLight
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(OceanBlue, TealAccent)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "KB",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Küçük Balina",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanBlue
                )
                Text(
                    "Kaz Bakım Asistanı",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }
            
            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Düzenle",
                    tint = TealAccent
                )
            }
        }
    }
}

@Composable
fun AppearanceSection(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                "Görünüm",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OceanBlue
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingItem(
                icon = Icons.Default.DarkMode,
                title = "Karanlık Mod",
                description = "Uygulama temasını değiştir",
                trailing = {
                    Switch(
                        checked = darkMode,
                        onCheckedChange = onDarkModeChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TealAccent,
                            checkedTrackColor = TealAccent.copy(alpha = 0.5f),
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = TextSecondary.copy(alpha = 0.5f)
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun NotificationsSection(
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    soundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    vibrationEnabled: Boolean,
    onVibrationChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                "Bildirimler",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OceanBlue
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingItem(
                icon = Icons.Default.Notifications,
                title = "Bildirimler",
                description = "Bildirimleri aç/kapat",
                trailing = {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = onNotificationsChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TealAccent,
                            checkedTrackColor = TealAccent.copy(alpha = 0.5f),
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = TextSecondary.copy(alpha = 0.5f)
                        )
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingItem(
                icon = Icons.Default.VolumeUp,
                title = "Ses",
                description = "Alarm sesini aç/kapat",
                trailing = {
                    Switch(
                        checked = soundEnabled,
                        onCheckedChange = onSoundChange,
                        enabled = notificationsEnabled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TealAccent,
                            checkedTrackColor = TealAccent.copy(alpha = 0.5f),
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = TextSecondary.copy(alpha = 0.5f)
                        )
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingItem(
                icon = Icons.Default.Vibration,
                title = "Titreşim",
                description = "Alarm titreşimini aç/kapat",
                trailing = {
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = onVibrationChange,
                        enabled = notificationsEnabled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TealAccent,
                            checkedTrackColor = TealAccent.copy(alpha = 0.5f),
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = TextSecondary.copy(alpha = 0.5f)
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun DataSection() {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                "Veri",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OceanBlue
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingItem(
                icon = Icons.Default.Backup,
                title = "Veri Yedekle",
                description = "Tüm verileri yedekle",
                trailing = {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                onClick = { }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingItem(
                icon = Icons.Default.Restore,
                title = "Veri Geri Yükle",
                description = "Yedekten veri geri yükle",
                trailing = {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                onClick = { }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingItem(
                icon = Icons.Default.Delete,
                title = "Verileri Temizle",
                description = "Tüm verileri sil",
                trailing = {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = ErrorRed
                    )
                },
                onClick = { }
            )
        }
    }
}

@Composable
fun AboutSection() {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                "Hakkında",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OceanBlue
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingItem(
                icon = Icons.Default.Info,
                title = "Uygulama Bilgisi",
                description = "Sürüm 1.0.0",
                trailing = {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                onClick = { }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingItem(
                icon = Icons.Default.Star,
                title = "Uygulamayı Değerlendir",
                description = "Google Play'de değerlendir",
                trailing = {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                onClick = { }
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            SettingItem(
                icon = Icons.Default.Share,
                title = "Paylaş",
                description = "Uygulamayı arkadaşlarınla paylaş",
                trailing = {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                onClick = { }
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    trailing: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    OceanBlue.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = OceanBlue,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                description,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        trailing()
    }
}
