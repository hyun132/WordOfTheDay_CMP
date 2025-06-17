package org.hyun.projectkmp.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordResponse (
    val email:String
)