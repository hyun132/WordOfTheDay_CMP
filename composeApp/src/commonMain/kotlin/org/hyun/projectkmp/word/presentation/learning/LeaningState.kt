package org.hyun.projectkmp.word.presentation.learning

import org.hyun.projectkmp.core.presentation.UiText
import org.hyun.projectkmp.word.domain.Mode

data class LeaningState(
    val word: String = "",
    val sentenceItems: List<LearningSentenceItem> = emptyList<LearningSentenceItem>(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val progress: Int = 0,
    val totalSize: Int = 5,
    val mode: Mode = Mode.TEXT,
)
