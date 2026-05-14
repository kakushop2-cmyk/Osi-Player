package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

/**
 * A row with a label and a horizontal group of buttons for discrete values.
 *
 * @param label Display label
 * @param options List of (value, display text) pairs
 * @param selected Currently selected value
 * @param onSelect Called with newly selected value
 */
@Composable
fun <T> SegmentedRow(
    label: String,
    options: List<Pair<T, String>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = WarmTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            options.forEach { (value, text) ->
                val isSelected = value == selected
                Button(
                    onClick = { onSelect(value) },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) WarmGold else WarmSurfaceElevated,
                        contentColor = if (isSelected) Color.Black else WarmTextSecondary
                    )
                ) {
                    Text(
                        text = text,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
