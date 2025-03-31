package org.hyun.projectkmp.word.presentation.learning

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
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

            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                var text by remember { mutableStateOf("") }
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
                                        onAction(LearningAction.OnBookMarkClick(state.sentences[page]))
                                    }.align(Alignment.End),
                            )

                            Text(
                                text = state.sentences[page],
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
                        BasicTextField(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            value = text,
                            onValueChange = { text = it }
                        )
                    }

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