package org.hyun.projectkmp.word.presentation.history

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyun.projectkmp.core.presentation.DeepPurple
import org.hyun.projectkmp.core.presentation.LightGray
import org.hyun.projectkmp.core.presentation.LightPurple
import org.hyun.projectkmp.word.domain.LearningHistory
import org.hyun.projectkmp.word.presentation.components.ActionBar
import org.jetbrains.compose.resources.painterResource
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.arrow_left
import wordoftheday.composeapp.generated.resources.arrow_right

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
            ActionBar(title = "Learning records")

            MonthSelector(onAction, state)

            HistoryContents(state)
        }
    }
}

@Composable
private fun MonthSelector(
    onAction: (HistoryAction) -> Unit,
    state: HistoryState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrow_left),
                modifier = Modifier.size(20.dp)
                    .clickable {
                        onAction(HistoryAction.OnPreviousMonthClick(state.displayedYearMonth))
                    },
                contentDescription = "last month"
            )
            Text(
                modifier = Modifier.weight(1f),
                text = state.displayedYearMonth,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(Res.drawable.arrow_right),
                modifier = Modifier.size(20.dp).clickable {
                    onAction(HistoryAction.OnNextMonthClick(state.displayedYearMonth))
                },
                contentDescription = "next month"
            )
        }
    }
}

@Composable
private fun HistoryContents(state: HistoryState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, LightGray)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            // 그룹 헤더
            item {
                Text(
                    text = state.displayedYearMonth,
                    style = MaterialTheme.typography.titleSmall,
                    color = DeepPurple
                )
            }
            state.histories.keys.sortedDescending().forEach { key ->
                // 그룹 내 항목들
                items(state.histories[key]?: emptyList()) { history ->
                    HistoryItem(history)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(history: LearningHistory) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            border = BorderStroke(1.dp, LightPurple),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
        ) {
            Text(
                text = history.learnedAt.split("-")[2],
                style = MaterialTheme.typography.headlineSmall,
                color = DeepPurple,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = LightPurple)
        ) {
            Text(
                text = history.word,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
