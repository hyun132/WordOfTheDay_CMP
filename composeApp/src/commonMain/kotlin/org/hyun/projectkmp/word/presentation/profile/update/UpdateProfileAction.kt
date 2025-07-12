package org.hyun.projectkmp.word.presentation.profile.update

import org.hyun.projectkmp.word.domain.Difficulty

interface UpdateProfileAction {
    data object OnBackClick : UpdateProfileAction
    data object OnUpdateClick : UpdateProfileAction
    data object OnMenuExpendChanged : UpdateProfileAction
    data class OnNameChanged(val name: String) : UpdateProfileAction
    data class OnTopicChanged(val topic: String) : UpdateProfileAction
    data class OnDifficultyChanged(val difficulty: Difficulty) : UpdateProfileAction
}