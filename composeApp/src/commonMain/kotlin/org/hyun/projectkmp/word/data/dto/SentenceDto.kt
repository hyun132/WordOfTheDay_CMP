package org.hyun.projectkmp.word.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SentenceDto(
    val sentences: List<String>? = emptyList()
)
