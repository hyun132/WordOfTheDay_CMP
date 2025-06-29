package org.hyun.projectkmp.word.presentation.profile.create

import org.hyun.projectkmp.word.domain.Difficulty

data class CreateProfileState(
    val isLoading: Boolean = true,
    val isMenuExpended: Boolean = false,
    val name:String = "",
    val topic: String ="",
    val difficulty: Difficulty = Difficulty.BEGINNER
)