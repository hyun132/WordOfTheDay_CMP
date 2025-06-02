package org.hyun.projectkmp.word.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LearningHistoriesResponse(
    val learningHistories: List<LearningHistoryDto>,
    val learnedYearMonth: String,
)
