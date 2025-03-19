package org.hyun.projectkmp.word.presentation

sealed interface WordHomeAction {
    data class OnWordClick(val word: String) : WordHomeAction
    data object OnNewWordClick : WordHomeAction
}