package org.hyun.projectkmp.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    val accessToken: String,
    val refreshToken: String
)