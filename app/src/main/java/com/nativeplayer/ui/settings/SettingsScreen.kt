package com.nativeplayer.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.data.model.Account
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.splash.SplashViewModel
import com.nativeplayer.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val accounts = remember { viewModel.getAccounts() }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingAccount by remember { mutableStateOf<Account?>(null) }
    var showClearCacheDialog by remember { mutableStateOf(false) }
    var cacheCleared by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = { Text("Einstellungen", color = WarmTextPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Zurück", tint = WarmTextPrimary)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.loadData()
                        navController.navigate(Screen.Splash.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }) {
                        Text("Neu laden", color = WarmGold, fontSize = 13.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmSurface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Accounts section
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Accounts", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Filled.Add, "Account hinzufügen", tint = WarmGold)
                    }
                }
            }

            items(accounts) { account ->
                val isActive = viewModel.getActiveAccount()?.id == account.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setActiveAccount(account.id)
                            viewModel.loadData(account)
                            navController.navigate(Screen.Splash.route) {
                                popUpTo(Screen.Settings.route) { inclusive = true }
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) WarmGold.copy(alpha = 0.15f) else WarmSurface
                    ),
                    border = if (isActive) CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(WarmGold)
                    ) else null
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(account.name, color = WarmTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(account.domain, color = WarmTextSecondary, fontSize = 12.sp)
                                Text(account.username, color = WarmTextSecondary, fontSize = 11.sp)
                            }
                            if (isActive) {
                                Icon(Icons.Filled.CheckCircle, null, tint = WarmGold, modifier = Modifier.size(20.dp))
                            }
                            Spacer(Modifier.width(8.dp))
                            IconButton(onClick = { editingAccount = account }) {
                                Icon(Icons.Filled.Edit, "Bearbeiten", tint = WarmTextSecondary, modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = {
                                viewModel.deleteAccount(account.id)
                            }) {
                                Icon(Icons.Filled.Delete, "Löschen", tint = WarmError.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Bei Klick auf einen Account wird dieser aktiviert und die Daten neu geladen.",
                    color = WarmTextSecondary,
                    fontSize = 11.sp
                )
            }

            // ── Daten-Bereich ─────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                Text("Daten", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showClearCacheDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = if (cacheCleared) WarmGold else WarmTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "Weiterschauen löschen",
                                color = WarmTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = if (cacheCleared) "Cache wurde geleert" else "Fortschrittseinträge & Resume-Positionen entfernen",
                                color = if (cacheCleared) WarmGold else WarmTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = WarmTextSecondary.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // ── Player-Bereich ────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                Text("Player", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.PlayerSettings.route) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                            tint = WarmTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "Wiedergabe-Einstellungen",
                                color = WarmTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Seek, Geschwindigkeit, Puffer und mehr",
                                color = WarmTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = WarmTextSecondary.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    // Clear Watch History Dialog
    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            containerColor = WarmSurface,
            titleContentColor = WarmTextPrimary,
            title = {
                Text("Weiterschauen löschen?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Alle gespeicherten Fortschrittseinträge und Resume-Positionen werden unwiderruflich gelöscht.",
                    color = WarmTextSecondary,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearWatchHistory()
                        cacheCleared = true
                        showClearCacheDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmError)
                ) {
                    Text("Löschen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text("Abbrechen", color = WarmTextSecondary)
                }
            }
        )
    }

    // Add Account Dialog
    if (showAddDialog) {
        AccountDialog(
            title = "Account hinzufügen",
            initialAccount = null,
            onDismiss = { showAddDialog = false },
            onSave = { account ->
                viewModel.saveAccount(account)
                viewModel.setActiveAccount(account.id)
                showAddDialog = false
                viewModel.loadData(account)
                navController.navigate(Screen.Splash.route) {
                    popUpTo(Screen.Settings.route) { inclusive = true }
                }
            }
        )
    }

    // Edit Account Dialog
    editingAccount?.let { account ->
        AccountDialog(
            title = "Account bearbeiten",
            initialAccount = account,
            onDismiss = { editingAccount = null },
            onSave = { updated ->
                viewModel.saveAccount(updated)
                editingAccount = null
            }
        )
    }
}

@Composable
fun AccountDialog(
    title: String,
    initialAccount: Account?,
    onDismiss: () -> Unit,
    onSave: (Account) -> Unit
) {
    var name by remember { mutableStateOf(initialAccount?.name ?: "") }
    var domain by remember { mutableStateOf(initialAccount?.domain ?: "") }
    var username by remember { mutableStateOf(initialAccount?.username ?: "") }
    var password by remember { mutableStateOf(initialAccount?.password ?: "") }
    var showPassword by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = WarmSurface,
        titleContentColor = WarmTextPrimary,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = darkTextFieldColors(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                OutlinedTextField(
                    value = domain,
                    onValueChange = { domain = it },
                    label = { Text("Server URL") },
                    placeholder = { Text("http://domain.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = darkTextFieldColors(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Benutzername") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = darkTextFieldColors(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Passwort") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null,
                                tint = WarmTextSecondary
                            )
                        }
                    },
                    colors = darkTextFieldColors(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (name.isNotBlank() && domain.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
                                val account = Account(
                                    id = initialAccount?.id ?: "acc_${System.currentTimeMillis()}",
                                    name = name,
                                    domain = domain.trimEnd('/'),
                                    username = username,
                                    password = password
                                )
                                onSave(account)
                            }
                        }
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val account = Account(
                        id = initialAccount?.id ?: "acc_${System.currentTimeMillis()}",
                        name = name,
                        domain = domain.trimEnd('/'),
                        username = username,
                        password = password
                    )
                    onSave(account)
                },
                enabled = name.isNotBlank() && domain.isNotBlank() && username.isNotBlank() && password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = WarmGold)
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen", color = WarmTextSecondary)
            }
        }
    )
}

@Composable
fun darkTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = WarmTextPrimary,
    unfocusedTextColor = WarmTextPrimary,
    cursorColor = WarmGold,
    focusedBorderColor = WarmGold,
    unfocusedBorderColor = WarmTextSecondary.copy(alpha = 0.3f),
    focusedLabelColor = WarmGold,
    unfocusedLabelColor = WarmTextSecondary,
    focusedPlaceholderColor = WarmTextSecondary,
    unfocusedPlaceholderColor = WarmTextSecondary.copy(alpha = 0.5f),
    focusedContainerColor = WarmSurfaceElevated,
    unfocusedContainerColor = WarmSurfaceElevated
)
