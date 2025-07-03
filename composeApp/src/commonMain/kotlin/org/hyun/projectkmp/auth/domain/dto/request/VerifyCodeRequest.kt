package org.hyun.projectkmp.auth.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeRequest (
    val email:String,
    val code:String
)