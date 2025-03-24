package org.hyun.projectkmp.word.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SentencesDto(
    val sentences: List<String>? = emptyList()
)
