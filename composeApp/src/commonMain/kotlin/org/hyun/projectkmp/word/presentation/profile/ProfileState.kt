package org.hyun.projectkmp.word.presentation.profile

import org.hyun.projectkmp.word.domain.Difficulty

data class ProfileState(
    val isLoading: Boolean = true,
    val signUpDate: String = "",
    val learnedWordCount: Int = 0,
    val longestStreak: Int = 0,
    val username: String = "",
    val difficulty: Difficulty = Difficulty.BEGINNER,
    val topic: String = ""
)