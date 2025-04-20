package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable
import org.hyun.projectkmp.word.domain.Mode

@Serializable
data class LearningCompleteRequest(
    val word: String
)
