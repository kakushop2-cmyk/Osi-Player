package com.nativeplayer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays a grid of checkboxes for category visibility control.
 * Shows all available categories, with checked state for visible categories.
 * Unchecked = hidden category.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesCheckboxGrid(
    categories: List<String>,
    hiddenCategories: Set<String>,
    onCategoryToggle: (categoryName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isVisible = !hiddenCategories.contains(category)
            CategoryCheckboxItem(
                categoryName = category,
                isVisible = isVisible,
                onToggle = { onCategoryToggle(category) }
            )
        }
    }
}

/**
 * Individual checkbox item for a single category.
 * Checked = visible, Unchecked = hidden.
 */
@Composable
fun CategoryCheckboxItem(
    categoryName: String,
    isVisible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isVisible,
            onCheckedChange = { onToggle() }
        )
        Text(
            text = categoryName,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
