package com.nativeplayer.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nativeplayer.ui.components.CategoriesCheckboxGrid
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel

/**
 * Dialog zum Ein-/Ausblenden von Kategorien für Live TV, Filme und Serien.
 */
@Composable
fun CategoryVisibilityDialog(
    title: String,
    categoryType: String, // "live", "movies", "series"
    availableCategories: List<String>,
    hiddenCategories: Set<String>,
    onDismiss: () -> Unit,
    categoryVisibilityViewModel: CategoryVisibilityViewModel
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSurface)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WarmSurfaceElevated)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = when (categoryType) {
                        "live" -> Icons.Filled.Tv
                        "movies" -> Icons.Filled.Movie
                        "series" -> Icons.Filled.VideoLibrary
                        else -> Icons.Filled.Category
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = WarmGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmTextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${availableCategories.size - hiddenCategories.size}/${availableCategories.size}",
                        fontSize = 14.sp,
                        color = WarmTextSecondary
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Schließen",
                            tint = WarmTextSecondary
                        )
                    }
                }

                HorizontalDivider(color = WarmTextSecondary.copy(alpha = 0.1f))

                // Content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (availableCategories.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null,
                                    tint = WarmTextSecondary.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "Keine Kategorien geladen.",
                                    color = WarmTextSecondary,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Bitte Daten neu laden.",
                                    color = WarmTextSecondary.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            item {
                                Text(
                                    text = "Aktiviere oder deaktiviere Kategorien, um sie in der App anzuzeigen oder auszublenden.",
                                    color = WarmTextSecondary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }

                            items(availableCategories) { category ->
                                CategoryToggleItem(
                                    categoryName = category,
                                    isVisible = !hiddenCategories.contains(category),
                                    onToggle = {
                                        categoryVisibilityViewModel.toggleCategoryVisibility(
                                            categoryType,
                                            category
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                // Footer
                HorizontalDivider(color = WarmTextSecondary.copy(alpha = 0.1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WarmSurfaceElevated)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = WarmGold
                        )
                    ) {
                        Text("Fertig", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

/**
 * Einzelnes Toggle-Item für eine Kategorie im Dialog.
 */
@Composable
private fun CategoryToggleItem(
    categoryName: String,
    isVisible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onToggle() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isVisible) WarmGold.copy(alpha = 0.1f) else WarmSurfaceElevated
        ),
        border = if (isVisible) {
            CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(WarmGold.copy(alpha = 0.3f))
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isVisible,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = WarmGold,
                    uncheckedColor = WarmTextSecondary.copy(alpha = 0.5f)
                )
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = categoryName,
                fontSize = 14.sp,
                fontWeight = if (isVisible) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isVisible) WarmTextPrimary else WarmTextSecondary
            )
            Spacer(Modifier.weight(1f))
            if (isVisible) {
                Icon(
                    imageVector = Icons.Filled.Visibility,
                    contentDescription = "Sichtbar",
                    tint = WarmGold,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.VisibilityOff,
                    contentDescription = "Ausgeblendet",
                    tint = WarmTextSecondary.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
