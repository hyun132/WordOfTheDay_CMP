package org.hyun.projectkmp.word.presentation.history

sealed interface HistoryAction {
    data class OnMonthChange(val yearMonth: String) : HistoryAction
    data object OnBackClick : HistoryAction
}