package org.hyun.projectkmp.word.presentation.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightGray
import org.hyun.projectkmp.core.presentation.UiEffect
import org.hyun.projectkmp.word.presentation.components.ActionBar
import org.jetbrains.compose.resources.stringResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.learned_word_count
import wordoftheday.composeapp.generated.resources.learning_profile

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel,
    showSnackBar: (String) -> Unit,
    navigateTo: (Routes) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UiEffect.ShowError -> {
                    showSnackBar(effect.message)
                }

                is UiEffect.NavigateTo -> {
                    navigateTo(effect.destination)
                }

                else -> Unit
            }
        }
    }

    ProfileScreen(
        state = state,
        onAction = {
            viewModel.onAction(it)
        }
    )

}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8E8E8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ActionBar(title = stringResource(Res.string.learning_profile), rightIcon = {
                Icon(imageVector = Icons.Default.Edit, "edit", modifier = Modifier.clickable {
                    onAction(
                        ProfileAction.OnEditClick
                    )
                })
            })

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, LightGray)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = state.username,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )

                    Text(
                        text = "since ${state.signUpDate}", //"since 23 March 2025",
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
                        NumberContents(
                            title = stringResource(Res.string.learned_word_count),
                            number = state.learnedWordCount.toString(),
                            modifier = Modifier.Companion.weight(1f)
                        )
                    }

                    RowContents("difficulty", state.difficulty.name)

                    RowContents("subject/topic", state.topic)
                }
            }
        }
    }
}

@Composable
fun NumberContents(title: String, number: String, modifier: Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = number,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun RowContents(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                color = DeepPurple
            )
        }
    }
}