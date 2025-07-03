package org.hyun.projectkmp.auth.domain.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeResponse(
    val verified: Boolean
)