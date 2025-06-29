package org.hyun.projectkmp.word.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.UiEffect
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
            getProfile()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onAction(action: WordHomeAction) {
        when (action) {
            is WordHomeAction.OnNewWordClick -> {
                getNewWord(state.value.subject, state.value.difficulty)
            }

            else -> Unit
        }
    }

    private fun getLocalData() = viewModelScope.launch {
        val subject = localRepository.getSubject()
        val difficulty = localRepository.getDifficulty()?.let { Difficulty.valueOf(it) }
        if (subject.isNullOrEmpty() || difficulty?.name.isNullOrEmpty()) {
            _effect.emit(UiEffect.NavigateTo(Routes.CreateProfile))
            return@launch
        }
        _state.update {
            it.copy(
                subject = subject,
                difficulty = difficulty!!
            )
        }
        getTodaysWord(subject, difficulty!!)
    }

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            wordRepository.getProfile()
                .onSuccess { response ->
                    localRepository.saveName(response.username)
                    localRepository.saveDifficulty(response.difficulty)
                    localRepository.saveTopic(response.topic)
                    _state.update {
                        it.copy(
                            subject = response.topic,
                            difficulty = Difficulty.valueOf(response.difficulty),
                            isLoading = false
                        )
                    }
                    getTodaysWord(response.topic, Difficulty.valueOf(response.difficulty))
                }.onError {
                    getLocalData()
                }
        }
    }

    private fun getTodaysWord(subject: String, difficulty: Difficulty) = viewModelScope.launch {
        wordRepository.getTodaysWord(
            requestQuery = WordRequestQuery(
                subject = subject,
                difficulty = difficulty
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

    private fun getNewWord(subject: String, difficulty: Difficulty) = viewModelScope.launch {
        wordRepository.getNewWord(
            requestQuery = WordRequestQuery(
                subject = subject,
                difficulty = difficulty
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