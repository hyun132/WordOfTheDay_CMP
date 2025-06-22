package org.hyun.projectkmp.auth.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class InfoResponse (
    val email:String,
    val createdAt:String,
)