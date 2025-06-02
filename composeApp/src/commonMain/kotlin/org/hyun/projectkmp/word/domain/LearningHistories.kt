package org.hyun.projectkmp.word.domain

data class LearningHistories(
    val learningHistories: Map<String,List<LearningHistory>>,
    val yearMonth: String,
)
