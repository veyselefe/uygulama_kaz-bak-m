package com.kucukbalina

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.kucukbalina.ui.screens.*
import com.kucukbalina.ui.theme.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Permission result handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        
        setContent {
            KucukBalinaTheme {
                KucukBalinaApp()
            }
        }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")

private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

@Composable
fun KucukBalinaApp() {
    var showSplash by remember { mutableStateOf(true) }
    var showOnboarding by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf(0) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Check if onboarding was completed
    LaunchedEffect(Unit) {
        val dataStore = context.dataStore
        val preferences = dataStore.data.first()
        showOnboarding = preferences[ONBOARDING_COMPLETED_KEY] != true
    }
    
    if (showSplash) {
        SplashScreen(
            onSplashComplete = {
                showSplash = false
            }
        )
    } else if (showOnboarding) {
        OnboardingScreen(
            onOnboardingComplete = {
                coroutineScope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[ONBOARDING_COMPLETED_KEY] = true
                    }
                }
                showOnboarding = false
            }
        )
    } else {
        val screens = listOf(
            "Nöbet" to Icons.Default.Event,
            "Sıra" to Icons.Default.People,
            "Yem" to Icons.Default.Restaurant,
            "Su" to Icons.Default.WaterDrop,
            "İstatistik" to Icons.Default.BarChart,
            "Ayarlar" to Icons.Default.Settings
        )
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = OceanBlue,
                tonalElevation = 8.dp
            ) {
                screens.forEachIndexed { index, (label, icon) ->
                    NavigationBarItem(
                        selected = selectedScreen == index,
                        onClick = { selectedScreen = index },
                        icon = {
                            Icon(
                                icon,
                                contentDescription = label,
                                tint = if (selectedScreen == index) TealAccent else Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                label,
                                color = if (selectedScreen == index) TealAccent else Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp,
                                fontWeight = if (selectedScreen == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealAccent,
                            selectedTextColor = TealAccent,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = TealAccent.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedScreen) {
                0 -> DutyScheduleScreen()
                1 -> DutyRotationScreen()
                2 -> FeedScheduleScreen()
                3 -> WaterScheduleScreen()
                4 -> StatisticsScreen()
                5 -> SettingsScreen()
            }
        }
    }
    }
}
