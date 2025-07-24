package org.hyun.projectkmp.word.presentation.learning

import org.hyun.projectkmp.word.domain.Word

sealed interface LearningAction {
    data class OnBookMarkClick(val sentence: String, val isBookmarked:Boolean) : LearningAction
    data class OnScroll(val skipTo: Int) : LearningAction
    data class OnSubmit(val word:Word) : LearningAction
    data class OnTextChange(val text:String) : LearningAction
    data class OnAudioStartClick(val word:Word) : LearningAction
    data object OnBackClick : LearningAction
    data object OnNext : LearningAction
    data object OnDoneClick : LearningAction
    data object OnDismiss : LearningAction
    data object OnModeClick : LearningAction
}