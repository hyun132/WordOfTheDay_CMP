package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable
import org.hyun.projectkmp.word.domain.Mode

@Serializable
data class AnswerResult(
    val isCorrect: Boolean,
    val similarity: Float,
    val correctAnswer: String,
    val userInput: String,
    val mode: Mode, // enum class Mode { TEXT, VOICE }
    val feedback: String
)
