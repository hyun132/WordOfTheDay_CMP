package org.hyun.projectkmp.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest (
    val currentPassword: String,
    val newPassword: String
)