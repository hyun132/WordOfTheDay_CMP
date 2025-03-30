package org.hyun.projectkmp.word.presentation.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.word.presentation.components.LineProgressBar

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
            Text(
                text = state.word
            )
        }
        LineProgressBar(state.progress, state.totalSize)
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(state.sentences) { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .clickable {
                                onAction(LearningAction.OnBookMarkClick(item))
                            }.fillMaxWidth()
                            .background(Color.Blue)
                    )
                }
            }

            Text(
                "tab to Next",
                modifier = Modifier
                    .clickable {
                        onAction(LearningAction.OnNext)
                    })
        }

    }
}