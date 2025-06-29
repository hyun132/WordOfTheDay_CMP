package org.hyun.projectkmp.word.presentation.profile.create

import org.hyun.projectkmp.word.domain.Difficulty

interface CreateProfileAction {
    data object OnBackClick : CreateProfileAction
    data object OnCreateClick : CreateProfileAction
    data object OnMenuExpendChanged : CreateProfileAction
    data class OnNameChanged(val name: String) : CreateProfileAction
    data class OnTopicChanged(val topic: String) : CreateProfileAction
    data class OnDifficultyChanged(val difficulty: Difficulty) : CreateProfileAction

}