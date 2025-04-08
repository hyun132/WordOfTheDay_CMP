package org.hyun.projectkmp.word.presentation.learning

data class LearningSentenceItem(
    val sentence: String,
    val userInput: String = "",
    val isCorrect: Boolean? = null
)
