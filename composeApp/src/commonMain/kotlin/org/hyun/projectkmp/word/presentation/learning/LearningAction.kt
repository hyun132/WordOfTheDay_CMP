package org.hyun.projectkmp.word.presentation.learning

sealed interface LearningAction {
    data class OnBookMarkClick(val sentence: String) : LearningAction
    data class OnScroll(val skipTo: Int) : LearningAction
    data class OnTextChange(val text:String) : LearningAction
    data object OnBackClick : LearningAction
    data object OnNext : LearningAction
    data object OnDoneClick : LearningAction
    data object OnSubmit : LearningAction
    data object OnDismiss : LearningAction
    data object OnModeClick : LearningAction
    data object OnAudioStartClick : LearningAction
}