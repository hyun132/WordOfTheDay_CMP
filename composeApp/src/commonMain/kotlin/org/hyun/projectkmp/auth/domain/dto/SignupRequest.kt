package org.hyun.projectkmp.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest (
    val email:String,
    val password:String,
)