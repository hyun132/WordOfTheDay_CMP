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
import org.hyun.projectkmp.word.domain.Difficulty

class WordHomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(WordHomeState())
    val state: StateFlow<WordHomeState> = _state
        .onStart {
            getNewWord()
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

    private fun getNewWord(difficulty: Difficulty = Difficulty.BEGINNER) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                word = "kotlin"
            )
        }
    }
}