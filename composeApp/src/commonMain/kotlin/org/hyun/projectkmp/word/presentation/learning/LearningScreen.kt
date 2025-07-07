package org.hyun.projectkmp.word.presentation.learning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightGray
import org.hyun.projectkmp.core.presentation.LightPurple
import org.hyun.projectkmp.word.domain.Mode
import org.hyun.projectkmp.word.presentation.WordHomeState
import org.hyun.projectkmp.word.presentation.WordHomeViewModel
import org.hyun.projectkmp.word.presentation.components.LineProgressBar
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.celebration
import wordoftheday.composeapp.generated.resources.checked
import wordoftheday.composeapp.generated.resources.close_dialog
import wordoftheday.composeapp.generated.resources.filled_star
import wordoftheday.composeapp.generated.resources.learning_completed
import wordoftheday.composeapp.generated.resources.play
import wordoftheday.composeapp.generated.resources.rotate_left
import wordoftheday.composeapp.generated.resources.star
import wordoftheday.composeapp.generated.resources.stop
import wordoftheday.composeapp.generated.resources.submit
import wordoftheday.composeapp.generated.resources.tab_to_next

@Composable
fun LearningScreenRoot(
    viewModel: LearningViewModel,
    wordState: WordHomeState,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(wordState) {
        viewModel.getSentences(wordState.word, wordState.meaning, wordState.difficulty)
    }

    LearningScreen(state = state, word = wordState.word) { action ->
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
fun LearningScreen(state: LeaningState, word: String, onAction: (LearningAction) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(LightPurple)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ActionBar(onAction, word, state)
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
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

                if (state.sentenceItems.isNotEmpty()) {
                    LineProgressBar(state.progress, state.totalSize)
                    SentenceContents(
                        state = state,
                        pagerState = pagerState,
                        word = word,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }
            }
        }
    }
}

@Composable
fun ActionBar(
    onAction: (LearningAction) -> Unit,
    word: String,
    state: LeaningState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "",
            modifier = Modifier
                .clickable {
                    onAction(LearningAction.OnBackClick)
                }
                .size(32.dp)
        )
        Text(
            text = word,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            color = DeepPurple,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = if (state.mode == Mode.TEXT) Mode.VOICE.name else Mode.TEXT.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable {
                    onAction(LearningAction.OnModeClick)
                }
        )
    }
}

@Composable
fun SentenceContents(
    state: LeaningState,
    pagerState: PagerState,
    word: String,
    onAction: (LearningAction) -> Unit,
    modifier: Modifier
) {
    VerticalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val currentItem = state.sentenceItems[page]
                SentenceContent(currentItem.sentence, currentItem.isBookmarked) {
                    onAction(
                        LearningAction.OnBookMarkClick(
                            currentItem.sentence,
                            currentItem.isBookmarked
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (state.mode == Mode.TEXT) {
                    if (currentItem.isCorrect == true) {
                        Icon(
                            painter = painterResource(Res.drawable.checked),
                            contentDescription = "check",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(32.dp),
                            tint = DeepPurple,
                        )
                    } else {
                        OutlinedTextField(
                            value = state.sentenceItems[state.progress].userInput,
                            onValueChange = {
                                onAction(LearningAction.OnTextChange(it))
                            },
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onAction(LearningAction.OnSubmit(word))
                                }
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done  // 키보드의 엔터 버튼을 'Done'으로 바꿔줌
                            ),
                            modifier = Modifier.height(120.dp).fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.LightGray,
                                focusedBorderColor = LightPurple
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                onAction(LearningAction.OnSubmit(word))
                            },
                            border = BorderStroke(width = 2.dp, color = LightGray),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = stringResource(Res.string.submit),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        val img =
                            if (state.isRecording) imageResource(Res.drawable.stop)
                            else if (currentItem.isCorrect == true) imageResource(Res.drawable.rotate_left)
                            else imageResource(Res.drawable.play)
                        Button(
                            onClick = { onAction(LearningAction.OnAudioStartClick(word)) },
                            colors = ButtonDefaults.buttonColors(containerColor = LightPurple)
                        ) {
                            Icon(
                                bitmap = img,
                                contentDescription = "play",
                                modifier = Modifier
                                    .size(20.dp),
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (state.progress != state.totalSize - 1) {
                    Text(
                        stringResource(Res.string.tab_to_next),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAction(LearningAction.OnNext)
                            },
                        textAlign = TextAlign.Center
                    )
                }

                if (state.showDialog) {
                    DoneDialog {
                        onAction(LearningAction.OnBackClick)
                    }
                }
            }
        }
    }
}

@Composable
fun SentenceContent(
    sentence: String,
    bookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = sentence,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge,
            fontSize = 26.sp,
            color = DeepPurple,
            lineHeight = 30.sp
        )
        Icon(
            painter = if (bookmarked) painterResource(Res.drawable.filled_star) else painterResource(
                Res.drawable.star
            ),
            contentDescription = "bookmark",
            modifier = Modifier
                .clickable {
                    onBookmarkClick()
                }
                .size(20.dp),
        )
    }

}

@Composable
fun DoneDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = LightPurple)
            ) {
                Row(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.learning_completed),
                        textAlign = TextAlign.Center,
                        style = TextStyle.Default.copy(
                            color = Color.White,
                            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                            fontSize = 24.sp
                        ),
                    )
                    Spacer(Modifier.width(8.dp))
                    Image(
                        bitmap = imageResource(Res.drawable.celebration),
                        contentDescription = "",
                        modifier = Modifier
                            .size(48.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDismiss()
                    }
            ) {
                Text(
                    stringResource(Res.string.close_dialog),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "close icon",
                    tint = Color.White
                )
            }
        }
    }
}
