package org.hyun.projectkmp.word.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightGray

@Composable
fun LineProgressBar(
    current: Int,
    total: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        val progress = current + 1
        Row(
            modifier = Modifier
                .weight(progress / total.toFloat()),
        ) {
            repeat(progress) {
                Dot(DeepPurple)
            }
        }
        if (progress < total) {
            Row(
                modifier = Modifier
                    .weight(1 - progress / total.toFloat()),
            ) {
                repeat(total - progress) {
                    Dot(LightGray)
                }
            }
        }
    }

}

@Composable
fun Dot(color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .fillMaxWidth()
            .height(10.dp)
    )
}