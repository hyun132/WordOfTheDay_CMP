package org.hyun.projectkmp.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val email:String,
    val password:String,
)