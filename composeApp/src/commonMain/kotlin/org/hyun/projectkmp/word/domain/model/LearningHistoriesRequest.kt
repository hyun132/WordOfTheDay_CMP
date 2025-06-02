package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LearningHistoriesRequest(
    val yearMonth:String
)
