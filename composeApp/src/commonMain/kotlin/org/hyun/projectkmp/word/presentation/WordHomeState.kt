package org.hyun.projectkmp.word.presentation

import org.hyun.projectkmp.core.presentation.UiText

data class WordHomeState(
    val word: String = "",
    val isLearned: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null
)