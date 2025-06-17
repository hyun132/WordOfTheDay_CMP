package org.hyun.projectkmp.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    val email:String,
    val createdAt:String,
)