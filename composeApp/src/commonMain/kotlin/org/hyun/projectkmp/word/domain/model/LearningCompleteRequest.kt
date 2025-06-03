package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LearningCompleteRequest(
    val word: String
)
