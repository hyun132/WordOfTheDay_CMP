package org.hyun.projectkmp.word.presentation

import org.hyun.projectkmp.core.presentation.UiText
import org.hyun.projectkmp.word.domain.Difficulty

data class WordHomeState(
    val word: String = "",
    val subject: String = "",
    val difficulty: Difficulty = Difficulty.BEGINNER,
    val isLearned: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null
)