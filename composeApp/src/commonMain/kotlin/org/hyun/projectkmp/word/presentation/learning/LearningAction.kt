package org.hyun.projectkmp.word.presentation.learning

sealed interface LearningAction {
    data object OnBackClick:LearningAction
    data class OnBookMarkClick(val sentence: String):LearningAction
    data object OnNext:LearningAction
    data object OnDoneClick:LearningAction
}