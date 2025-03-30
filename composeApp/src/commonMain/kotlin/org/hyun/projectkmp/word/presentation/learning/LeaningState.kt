package org.hyun.projectkmp.word.presentation.learning

import org.hyun.projectkmp.core.presentation.UiText

data class LeaningState(
    val word: String = "",
    val sentences: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val progress: Int = 1,
    val totalSize : Int = 5
)
