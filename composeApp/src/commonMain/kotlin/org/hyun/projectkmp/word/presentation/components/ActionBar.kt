package org.hyun.projectkmp.word.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyun.projectkmp.core.presentation.DeepPurple

@Composable
fun ActionBar(
    leftIcon: @Composable () -> Unit = {},
    title: String,
    rightIcon: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leftIcon()
        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            color = DeepPurple,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        rightIcon()
    }
}