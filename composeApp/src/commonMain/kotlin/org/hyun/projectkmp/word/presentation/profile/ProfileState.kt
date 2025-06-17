package org.hyun.projectkmp.word.presentation.profile

import org.hyun.projectkmp.core.presentation.UiText

data class ProfileState(
    val isLoading: Boolean = true,
    val signUpDate: String = "",
    val learnedWordCount: Int = 0,
    val learnedSentenceCount: Int = 0,
    val errorMessage: UiText? = null
)