package org.hyun.projectkmp.word.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightPurple
import org.hyun.projectkmp.core.presentation.UiEffect
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.get_new_word
import wordoftheday.composeapp.generated.resources.home_title
import wordoftheday.composeapp.generated.resources.learning_start

@Composable
fun WordHomeScreenRoot(
    onWordClick: (String) -> Unit,
    viewModel: WordHomeViewModel = koinViewModel(),
    showSnackBar: (String) -> Unit,
    navigate: (Routes) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is UiEffect.NavigateTo -> navigate(it.destination)
                is UiEffect.ShowError -> showSnackBar(it.message)
            }
        }
    }

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
            .fillMaxWidth()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .background(shape = RoundedCornerShape(12.dp), color = Color.Transparent)
                    .border(width = 1.dp, color = DeepPurple, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Column {
                        Text(
                            text = state.word,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = state.meaning,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = DeepPurple,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),
                onClick = { onAction(WordHomeAction.OnWordClick(state.word)) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepPurple,
                    contentColor = Color.White
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAction(WordHomeAction.OnNewWordClick)
                        },
                    text = stringResource(Res.string.get_new_word),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = DeepPurple,
                        shape = RoundedCornerShape(12.dp)
                    )
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
}


@Preview
@Composable
fun PreviewHome() {
    WordHomeScreen(WordHomeState(), {})
}