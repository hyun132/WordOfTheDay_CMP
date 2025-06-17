package org.hyun.projectkmp.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse (
    val email:String,
    val createdAt:String,
)