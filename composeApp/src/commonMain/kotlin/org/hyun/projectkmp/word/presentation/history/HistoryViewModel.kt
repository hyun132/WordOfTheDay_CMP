package org.hyun.projectkmp.word.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.toUiText
import org.hyun.projectkmp.word.domain.model.LearningHistoriesRequest
import org.hyun.projectkmp.word.domain.repository.WordRepository

class HistoryViewModel(
    private val repository: WordRepository
) : ViewModel() {

    val _state = MutableStateFlow(HistoryState())
    val state = _state
        .onStart {
            getHistory(_state.value.displayedYearMonth)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.OnMonthChange -> {

            }

            is HistoryAction.OnNextMonthClick -> {
                val newYearMonth = getNextMonth(action.yearMonth)
                _state.update {
                    it.copy(displayedYearMonth = newYearMonth)
                }
                getHistory(newYearMonth)
            }

            is HistoryAction.OnPreviousMonthClick -> {
                val newYearMonth = getPrevMonth(action.yearMonth)
                _state.update {
                    it.copy(displayedYearMonth = newYearMonth)
                }
                getHistory(newYearMonth)
            }

            else -> Unit
        }
    }

    fun getHistory(yearMonth: String) {
        viewModelScope.launch {
            repository.getLearningHistories(LearningHistoriesRequest(yearMonth = yearMonth))
                .onSuccess { histories ->
                    _state.update {
                        it.copy(histories = HashMap(histories.learningHistories))
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.toUiText()
                        )
                    }
                }
        }
    }


    fun getPrevMonth(current: String): String {
        val current = yearMonthStringToLocalDate(current)
        val prev = current.minus(1, DateTimeUnit.MONTH)

        return prev.year.toString().padStart(4, '0') + "-" + prev.monthNumber.toString()
            .padStart(2, '0')
    }

    fun getNextMonth(current: String): String {
        val current = yearMonthStringToLocalDate(current)
        val next = current.plus(1, DateTimeUnit.MONTH)

        return next.year.toString().padStart(4, '0') + "-" + next.monthNumber.toString()
            .padStart(2, '0')
    }

    fun yearMonthStringToLocalDate(current: String): LocalDate {
        val parts = current.split("-")
        val year = parts[0].toInt()
        val month = parts[1].toInt()

        return LocalDate(year, month, 1)
    }
}