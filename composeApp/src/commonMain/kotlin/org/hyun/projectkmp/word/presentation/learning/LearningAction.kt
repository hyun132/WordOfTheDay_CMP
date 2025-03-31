package org.hyun.projectkmp.word.presentation.learning

sealed interface LearningAction {
    data class OnBookMarkClick(val sentence: String) : LearningAction
    data class OnScroll(val skipTo: Int) : LearningAction
    data object OnBackClick : LearningAction
    data object OnNext : LearningAction
    data object OnDoneClick : LearningAction
}