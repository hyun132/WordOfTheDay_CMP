package org.hyun.projectkmp.word.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.back

@Composable
fun HistoryScreenRoot(
    viewModel: HistoryViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HistoryScreen(state = state) { action ->
        when (action) {
            is HistoryAction.OnBackClick -> {
                onBackClick()
            }

            else -> Unit
        }
        viewModel.onAction(action = action)
    }
}

@Composable
fun HistoryScreen(state: HistoryState, onAction: (HistoryAction) -> Unit) {

    Row {
        Icon(
            painter = painterResource(Res.drawable.back),
            contentDescription = "",
            modifier = Modifier.clickable {
                onAction(HistoryAction.OnBackClick)
            })
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        state.histories.forEach { (key, list) ->

            // 그룹 헤더
            item {
                Text(
                    text = key,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // 그룹 내 항목들
            items(list) { history ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp)),
                ) {
                    Text(
                        text = history.word,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        modifier = Modifier.clickable {
                            // 삭제 동작
                        }
                    )
                }
            }
        }
    }
}