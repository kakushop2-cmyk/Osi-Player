package com.nativeplayer.ui.splash

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.data.model.Account
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            is SplashState.Success -> {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
            is SplashState.NoAccount -> {
                navController.navigate(Screen.Settings.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Preload Background Image (Layer 0 - Bottom)
        Image(
            painter = painterResource(id = R.drawable.splash_preload),
            contentDescription = "Splash Background",
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f),
            contentScale = ContentScale.FillWidth
        )

        // Content (Layer 1 - Top)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(32.dp)
                .zIndex(1f)
        ) {
            Spacer(Modifier.height(40.dp))

            when (val currentState = state) {
                is SplashState.Loading -> {
                    CircularProgressIndicator(
                        color = WarmGold,
                        modifier = Modifier.size(40.dp),
                        strokeWidth = 3.dp
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("Lade Daten...", color = WarmTextSecondary, fontSize = 13.sp)
                }

                is SplashState.LoadingWithProgress -> {
                    LinearProgressIndicator(
                        progress = { currentState.progress },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(4.dp),
                        color = WarmGold,
                        trackColor = WarmSurfaceElevated
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(currentState.message, color = WarmTextSecondary, fontSize = 13.sp)
                }

                is SplashState.Error -> {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = WarmError
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        currentState.message,
                        color = WarmError,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.loadData() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarmGold)
                        ) {
                            Text("Erneut versuchen")
                        }
                        OutlinedButton(
                            onClick = {
                                navController.navigate(Screen.Settings.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarmTextSecondary)
                        ) {
                            Text("Einstellungen")
                        }
                    }
                }

                is SplashState.NoAccount -> {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = WarmTextSecondary
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Kein Account konfiguriert",
                        color = WarmTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                is SplashState.Success -> {
                    // Success state - will auto-navigate
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = WarmGold
                    )
                }
            }
        }
    }
}
