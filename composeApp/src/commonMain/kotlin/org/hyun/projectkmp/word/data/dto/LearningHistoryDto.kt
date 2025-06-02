package org.hyun.projectkmp.word.data.dto

import kotlinx.serialization.Serializable
import org.hyun.projectkmp.word.domain.Difficulty

@Serializable
data class LearningHistoryDto(
    val learnedAt:String,
    val word:String
)
