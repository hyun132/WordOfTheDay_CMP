package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val username: String,
    val difficulty: String,
    val topic: String
)
