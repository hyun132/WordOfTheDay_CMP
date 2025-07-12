package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val username: String,
    val difficulty: String,
    val topic: String,
    val createdAt:String
)
