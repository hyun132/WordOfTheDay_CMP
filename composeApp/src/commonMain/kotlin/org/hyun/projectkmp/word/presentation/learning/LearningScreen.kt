package org.hyun.projectkmp.word.presentation.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightGray
import org.hyun.projectkmp.core.presentation.LightPurple
import org.hyun.projectkmp.word.presentation.components.LineProgressBar
import org.jetbrains.compose.resources.painterResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.back

@Composable
fun LearningScreenRoot(
    viewModel: LearningViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LearningScreen(state = state) { action ->
        when (action) {
            is LearningAction.OnBackClick -> {
                onBackClick()
            }

            else -> Unit
        }
        viewModel.onAction(action = action)
    }
}

@Composable
fun LearningScreen(state: LeaningState, onAction: (LearningAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(Res.drawable.back),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        onAction(LearningAction.OnBackClick)
                    }
                    .size(40.dp)
            )
            Text(
                text = state.word,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        LineProgressBar(state.progress, state.totalSize)
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            val pagerState = rememberPagerState(pageCount = { state.totalSize })
            LaunchedEffect(state.progress) {
                if (pagerState.currentPage != state.progress) {
                    pagerState.animateScrollToPage(state.progress)
                }
            }

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }
                    .distinctUntilChanged()
                    .collect { page ->
                        onAction(LearningAction.OnScroll(page))
                    }
            }

            if(state.sentenceItems.isNotEmpty())
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = LightGray)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 40.dp, start = 24.dp, end = 24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "bookmark",
                                modifier = Modifier
                                    .clickable {
                                        onAction(LearningAction.OnBookMarkClick(state.sentenceItems[page].sentence))
                                    }.align(Alignment.End),
                            )

                            Text(
                                text = state.sentenceItems[page].sentence,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 26.sp,
                                color = DeepPurple,
                                lineHeight = 30.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = LightPurple)
                    ) {
                            UnderlineTextField(
                                text = state.sentenceItems[state.progress].userInput,
                                onTextChange = {
                                    onAction(LearningAction.OnTextChange(it))
                                },
                                modifier = Modifier,
                                onAction = {
                                    onAction(LearningAction.OnSubmit)
                                },
                            )
                    }
                    Text(
                        "submit",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                println("submit clicked")
                                onAction(LearningAction.OnSubmit)
                            },
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "tab to Next",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAction(LearningAction.OnNext)
                            },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

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
                    val lineHeight = 28.sp.toPx()
                    for (i in 0 until lineCount) {
                        val y = lineHeight * (i + 1)
                        drawLine(
                            color = Color.White,
                            start = Offset(x = 0f, y = y),
                            end = Offset(x = size.width, y = y),
                            strokeWidth = 4.dp.toPx()
                        )
                    }
                },
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
                        .padding(4.dp)
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "enter here",
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 28.sp
            )
        )
    }
}