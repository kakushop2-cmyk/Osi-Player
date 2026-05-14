package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

@Composable
fun TextFieldRow(
    label: String,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = WarmTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = WarmTextSecondary.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = WarmTextPrimary,
                unfocusedTextColor = WarmTextPrimary,
                cursorColor = WarmGold,
                focusedBorderColor = WarmGold,
                unfocusedBorderColor = WarmTextSecondary.copy(alpha = 0.3f),
                focusedLabelColor = WarmGold,
                unfocusedLabelColor = WarmTextSecondary,
                focusedContainerColor = WarmSurfaceElevated,
                unfocusedContainerColor = WarmSurfaceElevated
            )
        )
    }
}
