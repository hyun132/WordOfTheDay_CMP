package org.hyun.projectkmp.word.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightGray
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.get_new_word
import wordoftheday.composeapp.generated.resources.home_title
import wordoftheday.composeapp.generated.resources.learning_start

@Composable
fun WordHomeScreenRoot(
    onWordClick: (String) -> Unit,
    viewModel: WordHomeViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    WordHomeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is WordHomeAction.OnWordClick -> onWordClick(action.word)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun WordHomeScreen(
    state: WordHomeState,
    onAction: (WordHomeAction) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(Res.string.home_title),
                fontSize = 24.sp,
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(color = LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.word,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.get_new_word),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable {
                        onAction(WordHomeAction.OnNewWordClick)
                    }
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = DeepPurple,
                    shape = RoundedCornerShape(12.dp)
                )
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            onClick = { onAction(WordHomeAction.OnWordClick(state.word)) },
            colors = ButtonDefaults.buttonColors(
                containerColor = DeepPurple,
                contentColor = Color.White
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(Res.string.learning_start),
                textAlign = TextAlign.Center
            )
        }
    }
}