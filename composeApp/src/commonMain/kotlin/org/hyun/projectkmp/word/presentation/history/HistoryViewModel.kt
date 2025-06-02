package org.hyun.projectkmp.word.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
}