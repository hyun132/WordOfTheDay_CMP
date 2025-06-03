package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BookMarksRequestQuery(
    val amount: Int = 10
)
