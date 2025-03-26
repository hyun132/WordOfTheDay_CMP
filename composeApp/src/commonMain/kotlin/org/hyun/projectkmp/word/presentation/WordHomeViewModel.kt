package org.hyun.projectkmp.word.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.toUiText
import org.hyun.projectkmp.word.data.repository.LocalRepository
import org.hyun.projectkmp.word.domain.Difficulty
import org.hyun.projectkmp.word.domain.model.WordRequestQuery
import org.hyun.projectkmp.word.domain.repository.WordRepository

class WordHomeViewModel(
    private val wordRepository: WordRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WordHomeState())
    val state: StateFlow<WordHomeState> = _state
        .onStart {
            getLocalData()
            getTodaysWord()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    fun onAction(action: WordHomeAction) {
        when (action) {
            is WordHomeAction.OnNewWordClick -> {
                getNewWord()
            }

            else -> Unit
        }
    }

    private fun getLocalData() = viewModelScope.launch {
        val subject = localRepository.getSubject() ?: "business"
        val difficulty = localRepository.getDifficulty()?.let { Difficulty.valueOf(it) } ?: Difficulty.BEGINNER
        _state.update {
            it.copy(
                subject = subject,
                difficulty = difficulty
            )
        }
    }

    private fun getTodaysWord() = viewModelScope.launch {
        wordRepository.getTodaysWord(
            requestQuery = WordRequestQuery(
                subject = state.value.subject,
                difficulty = state.value.difficulty
            )
        )
            .onSuccess { word ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        word = word.word
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        word = "Today's Word",
                        errorMessage = error.toUiText()
                    )
                }
            }
    }

    private fun getNewWord() = viewModelScope.launch {
        wordRepository.getNewWord(
            requestQuery = WordRequestQuery(
                subject = state.value.subject,
                difficulty = state.value.difficulty
            )
        )
            .onSuccess { word ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        word = word.word
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }
}