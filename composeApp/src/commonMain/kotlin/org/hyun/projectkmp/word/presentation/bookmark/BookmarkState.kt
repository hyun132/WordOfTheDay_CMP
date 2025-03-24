package org.hyun.projectkmp.word.presentation.bookmark

import org.hyun.projectkmp.core.presentation.UiText

data class BookmarkState(
    val sentences: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null
)
