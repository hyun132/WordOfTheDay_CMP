package org.hyun.projectkmp.auth.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    var email: String,
    var username: String,
    var password: String,
)
