package org.hyun.projectkmp.word.domain.model

import kotlinx.serialization.Serializable
import org.hyun.projectkmp.word.domain.Difficulty

@Serializable
data class BookMarksRequestQuery(
    val amount: Int = 10
)
