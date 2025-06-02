package org.hyun.projectkmp.word.presentation.history

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hyun.projectkmp.core.presentation.UiText
import org.hyun.projectkmp.word.domain.LearningHistory

data class HistoryState(
    val displayedYearMonth: String = with(
        (Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date)
    ) {
        "${year}-${monthNumber.toString().padStart(2, '0')}"
    },
    val histories: HashMap<String, List<LearningHistory>> = hashMapOf(),
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null
)