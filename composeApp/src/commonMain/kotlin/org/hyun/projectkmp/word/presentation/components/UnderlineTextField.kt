package org.hyun.projectkmp.word.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyun.projectkmp.core.presentation.LightPurple


@Composable
fun UnderlineTextField(
    text: String,
    modifier: Modifier,
    onTextChange: (String) -> Unit,
    onAction: () -> Unit
) {
    var lineCount by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .background(LightPurple)
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val lineHeight = 30.sp.toPx()
                    for (i in 0 until lineCount) {
                        val y = lineHeight * (i + 1)
                        drawLine(
                            color = Color.White,
                            start = Offset(x = 0f, y = y),
                            end = Offset(x = size.width, y = y),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }
                .padding(bottom = 4.dp, top = 2.dp),
            onTextLayout = {
                lineCount = it.lineCount
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    onAction()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done  // 키보드의 엔터 버튼을 'Done'으로 바꿔줌
            ),
            cursorBrush = SolidColor(Color.Black),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "enter here",
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = TextStyle(
                                lineHeight = 30.sp
                            )
                        )
                    }
                    innerTextField()
                }
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 30.sp
            )
        )
    }
}