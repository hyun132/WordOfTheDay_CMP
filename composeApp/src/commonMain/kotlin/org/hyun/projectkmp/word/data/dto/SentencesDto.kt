package org.hyun.projectkmp.word.data.dto

import kotlinx.serialization.Serializable
import org.hyun.projectkmp.word.domain.Sentence

@Serializable
data class SentencesDto(
    val sentences: List<SentenceDto>? = emptyList()
)
