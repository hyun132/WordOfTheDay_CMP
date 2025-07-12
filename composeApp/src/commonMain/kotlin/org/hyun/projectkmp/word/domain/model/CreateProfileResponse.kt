package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileResponse(
    val username: String,
    val difficulty: String,
    val topic: String,
    val createdAt:String,
)
