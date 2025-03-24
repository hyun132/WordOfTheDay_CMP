package org.hyun.projectkmp.word.presentation.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BookMarkListScreenRoot(
    viewModel: BookmarkViewModel,
    onBackClick: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    BookmarkListScreen(state = state, action = {
        when (it) {
            is BookmarkAction.OnBackClick -> {
                onBackClick()
            }

            else -> Unit
        }
        viewModel.onAction(it)
    })
}

@Composable
fun BookmarkListScreen(
    state: BookmarkState,
    action: (BookmarkAction) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.Yellow),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(state.sentences) {
            Text(text = it)
        }
    }
}