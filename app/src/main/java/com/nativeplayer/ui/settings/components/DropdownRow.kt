package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

/**
 * A row with a label and an ExposedDropdownMenuBox.
 *
 * @param label Display label
 * @param options List of (value, display text) pairs
 * @param selected Currently selected value
 * @param onSelect Called with newly selected value
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownRow(
    label: String,
    options: List<Pair<T, String>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = options.find { it.first == selected }?.second ?: ""

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = WarmTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = WarmTextPrimary,
                    unfocusedTextColor = WarmTextPrimary,
                    focusedBorderColor = WarmGold,
                    unfocusedBorderColor = WarmTextSecondary.copy(alpha = 0.3f),
                    focusedContainerColor = WarmSurfaceElevated,
                    unfocusedContainerColor = WarmSurfaceElevated
                ),
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                options.forEach { (value, text) ->
                    DropdownMenuItem(
                        text = { Text(text, color = WarmTextPrimary) },
                        onClick = {
                            onSelect(value)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}
