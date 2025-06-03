package org.hyun.projectkmp.word.presentation.history

sealed interface HistoryAction {
    data class OnMonthChange(val yearMonth: String) : HistoryAction
    data class OnPreviousMonthClick(val yearMonth: String) : HistoryAction
    data class OnNextMonthClick(val yearMonth: String) : HistoryAction
    data object OnBackClick : HistoryAction
}