package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary

/**
 * A row with a label, a Slider, and a value display.
 * D-Pad left/right adjusts value by [step].
 *
 * @param label Display label
 * @param value Current value (in valueRange units)
 * @param valueRange Valid range for the slider
 * @param step Step size for D-Pad navigation
 * @param valueText Formatted display string (e.g., "30 s")
 * @param onValueChange Called with new value while sliding or via D-Pad
 */
@Composable
fun SliderRow(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    valueText: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = WarmTextPrimary,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = valueText,
                color = WarmGold,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        when (event.key) {
                            Key.DirectionLeft -> {
                                val newVal = (value - step).coerceIn(
                                    valueRange.start, valueRange.endInclusive
                                )
                                onValueChange(newVal)
                                true
                            }
                            Key.DirectionRight -> {
                                val newVal = (value + step).coerceIn(
                                    valueRange.start, valueRange.endInclusive
                                )
                                onValueChange(newVal)
                                true
                            }
                            else -> false
                        }
                    } else false
                },
            colors = SliderDefaults.colors(
                thumbColor = WarmGold,
                activeTrackColor = WarmGold,
                inactiveTrackColor = WarmSurfaceElevated
            )
        )
    }
}
