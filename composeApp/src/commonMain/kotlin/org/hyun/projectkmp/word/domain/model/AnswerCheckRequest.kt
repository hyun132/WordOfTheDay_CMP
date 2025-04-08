package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable
import org.hyun.projectkmp.word.domain.Mode

@Serializable
data class AnswerCheckRequest(
    val origin:String,
    val userAnswer:String,
    val mode:Mode
)
