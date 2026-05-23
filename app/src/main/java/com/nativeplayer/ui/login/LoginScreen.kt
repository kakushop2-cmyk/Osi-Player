package com.nativeplayer.ui.login

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.ui.navigation.Screen

// ── Play-Circle Logo ───────────────────────────────────────────
/**
 * Gold circle emblem with dark play triangle — serves as the "O" in "Osi Player".
 */
@Composable
fun PlayCircleLogo(modifier: Modifier = Modifier, size: Dp = 46.dp) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(8.dp, CircleShape, ambientColor = WarmGold.copy(alpha = 0.3f))
            .clip(CircleShape)
            .background(WarmGold),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.4f).padding(start = 1.dp)) {
            val w = this.size.width
            val h = this.size.height
            val path = Path().apply {
                moveTo(w * 0.15f, h * 0.1f)
                lineTo(w * 0.85f, h * 0.5f)
                lineTo(w * 0.15f, h * 0.9f)
                close()
            }
            drawPath(path, color = WarmBackground)
        }
    }
}

// ── App Logo + Wordmark ────────────────────────────────────────
/**
 * Branding: Play-Circle emblem replaces the "O" in "Osi Player".
 * Renders as: [▶ Play-Kreis] + "si" + " Player"
 */
@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayCircleLogo(size = 46.dp)
        Spacer(Modifier.width(1.dp))
        Text(
            text = "si",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = WarmGold,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = " Player",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = WarmTextPrimary,
            letterSpacing = (-0.5).sp
        )
    }
}

// ── Segmented Tab Control ──────────────────────────────────────
@Composable
fun SegmentedControl(
    selectedTab: LoginTab,
    onTabSelected: (LoginTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(WarmSurface)
            .padding(4.dp)
    ) {
        SegmentedTab(
            text = "Xtream Codes",
            selected = selectedTab == LoginTab.XTREAM,
            onClick = { onTabSelected(LoginTab.XTREAM) },
            modifier = Modifier.weight(1f)
        )
        SegmentedTab(
            text = "M3U Playlist",
            selected = selectedTab == LoginTab.M3U,
            onClick = { onTabSelected(LoginTab.M3U) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SegmentedTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) WarmSurfaceElevated else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
            color = if (selected) WarmTextPrimary else WarmTextSecondary
        )
    }
}

// ── Dark TextField Colors helper ───────────────────────────────
@Composable
fun loginTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = WarmTextPrimary,
    unfocusedTextColor = WarmTextPrimary,
    cursorColor = WarmGold,
    focusedBorderColor = WarmGold,
    unfocusedBorderColor = WarmTextSecondary.copy(alpha = 0.15f),
    focusedLabelColor = WarmGold,
    unfocusedLabelColor = WarmTextSecondary,
    focusedPlaceholderColor = WarmTextSecondary,
    unfocusedPlaceholderColor = WarmTextSecondary.copy(alpha = 0.4f),
    focusedContainerColor = WarmSurfaceElevated,
    unfocusedContainerColor = WarmSurfaceElevated
)

// ── Main Login Screen ──────────────────────────────────────────
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Auto-navigate on success
    LaunchedEffect(state) {
        if (state is LoginState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Intro.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
            .imePadding()
    ) {
        // Subtle gold glow at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            WarmGold.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(36.dp))

            // Logo
            AppLogo()

            Spacer(Modifier.height(28.dp))

            // Segmented Control
            SegmentedControl(
                selectedTab = viewModel.selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )

            Spacer(Modifier.height(16.dp))

            // Animated form content
            AnimatedContent(
                targetState = viewModel.selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150)) togetherWith
                        fadeOut(animationSpec = tween(150))
                },
                label = "login_form"
            ) { tab ->
                when (tab) {
                    LoginTab.XTREAM -> XtreamForm(viewModel, focusManager)
                    LoginTab.M3U -> M3UForm(viewModel, focusManager)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Connect button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.connect()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = state !is LoginState.Loading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WarmGold),
                contentPadding = PaddingValues(0.dp)
            ) {
                AnimatedContent(
                    targetState = state is LoginState.Loading,
                    transitionSpec = {
                        fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                    },
                    label = "button_state"
                ) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            color = WarmBackground,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            "Verbinden",
                            color = WarmBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Error banner
            AnimatedVisibility(
                visible = state is LoginState.Error,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                val errorMessage = (state as? LoginState.Error)?.message ?: ""
                Text(
                    text = errorMessage,
                    color = WarmError,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Spacer(Modifier.height(36.dp))
        }
    }
}

// ── Xtream Codes form fields ───────────────────────────────────
@Composable
private fun XtreamForm(
    viewModel: LoginViewModel,
    focusManager: FocusManager
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = viewModel.serverUrl,
            onValueChange = { viewModel.onServerUrlChange(it) },
            label = { Text("Server URL") },
            placeholder = { Text("http://mein-server.com") },
            isError = viewModel.serverError != null,
            supportingText = viewModel.serverError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = loginTextFieldColors(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(10.dp)
        )
        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text("Benutzername") },
            isError = viewModel.usernameError != null,
            supportingText = viewModel.usernameError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = loginTextFieldColors(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            shape = RoundedCornerShape(10.dp)
        )
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Passwort") },
            isError = viewModel.passwordError != null,
            supportingText = viewModel.passwordError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (viewModel.showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { viewModel.onShowPasswordToggle() }) {
                    Icon(
                        if (viewModel.showPassword) Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility,
                        contentDescription = if (viewModel.showPassword) "Passwort verstecken"
                        else "Passwort anzeigen",
                        tint = WarmTextSecondary
                    )
                }
            },
            colors = loginTextFieldColors(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    viewModel.connect()
                }
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

// ── M3U playlist form field ────────────────────────────────────
@Composable
private fun M3UForm(
    viewModel: LoginViewModel,
    focusManager: FocusManager
) {
    OutlinedTextField(
        value = viewModel.m3uUrl,
        onValueChange = { viewModel.onM3uUrlChange(it) },
        label = { Text("M3U Playlist URL") },
        placeholder = { Text("https://meine-playlist.m3u") },
        isError = viewModel.m3uError != null,
        supportingText = viewModel.m3uError?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = loginTextFieldColors(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                viewModel.connect()
            }
        ),
        shape = RoundedCornerShape(10.dp)
    )
}
