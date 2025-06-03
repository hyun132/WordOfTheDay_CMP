package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LearningCompleteResponse(
    val word: String,
    val learnedAt: String
)
