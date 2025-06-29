package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileRequest(
    val username: String,
    val difficulty: String,
    val topic: String
)
