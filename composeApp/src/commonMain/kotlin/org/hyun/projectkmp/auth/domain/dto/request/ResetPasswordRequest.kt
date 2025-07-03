package org.hyun.projectkmp.auth.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)