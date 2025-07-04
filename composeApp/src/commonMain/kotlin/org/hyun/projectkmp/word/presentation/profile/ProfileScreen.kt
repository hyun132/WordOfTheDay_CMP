package org.hyun.projectkmp.word.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.core.presentation.LightPurple
import org.jetbrains.compose.resources.stringResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.learned_word_count

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel,
    onBackClick: (ProfileAction) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProfileScreen(
        state = state,
        onAction = {
            when (it) {
                is ProfileAction.OnBackClick -> onBackClick(it)
                else -> Unit
            }
            viewModel.onAction(it)
        }
    )

}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = state.username,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )

        Text(
            text = "since 23 March 2025",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = LightPurple)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
//                    Icon(
//                        painter = painterResource(Res.drawable.filled_star),
//                        contentDescription = "star",
//                        tint = Color.Yellow,
//                        modifier = Modifier.size(20.dp)
//                    )
                    Text(
                        text = stringResource(Res.string.learned_word_count),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    text = state.learnedWordCount.toString(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = LightPurple)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
//                    Icon(
//                        painter = painterResource(Res.drawable.filled_star),
//                        contentDescription = "star",
//                        tint = Color.Yellow,
//                        modifier = Modifier.size(20.dp)
//                    )
                    Text(
                        text = stringResource(Res.string.learned_word_count),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    text = state.learnedWordCount.toString(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("difficulty")
        Text(
            text = state.difficulty.name,
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text("subject/topic")
        Text(
            text = state.topic,
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            fontSize = 20.sp
        )
    }
}