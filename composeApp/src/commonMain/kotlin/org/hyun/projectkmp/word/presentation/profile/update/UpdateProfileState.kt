package org.hyun.projectkmp.word.presentation.profile.update

import org.hyun.projectkmp.word.domain.Difficulty

data class UpdateProfileState(
    val isLoading: Boolean = true,
    val isMenuExpended: Boolean = false,
    val name:String = "",
    val topic: String ="",
    val difficulty: Difficulty = Difficulty.BEGINNER
)