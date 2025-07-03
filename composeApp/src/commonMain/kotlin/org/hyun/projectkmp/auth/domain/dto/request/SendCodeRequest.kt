package org.hyun.projectkmp.auth.domain.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SendCodeRequest (
    val email:String
)