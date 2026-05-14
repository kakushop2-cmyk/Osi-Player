package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmTextSecondary

@Composable
fun SectionHeader(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 4.dp)
    ) {
        Text(
            text = title,
            color = WarmGold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
        Divider(
            modifier = Modifier.padding(top = 4.dp),
            color = WarmTextSecondary.copy(alpha = 0.15f),
            thickness = 1.dp
        )
    }
}
