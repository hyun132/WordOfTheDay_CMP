package org.hyun.projectkmp.word.presentation.learning

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.toUiText
import org.hyun.projectkmp.word.domain.Difficulty
import org.hyun.projectkmp.word.domain.Mode
import org.hyun.projectkmp.word.domain.model.AnswerCheckRequest
import org.hyun.projectkmp.word.domain.model.BookMarkRequestQuery
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.repository.WordRepository

class LearningViewModel(
    private val repository: WordRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val word = savedStateHandle.toRoute<Routes.Word>().word

    private val _state = MutableStateFlow(LeaningState())
    val state: StateFlow<LeaningState> = _state
        .onStart {
            _state.update {
                it.copy(word = word)
            }
            getSentences()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    fun onAction(action: LearningAction) {
        when (action) {
            is LearningAction.OnBookMarkClick -> {
                bookMark(action.sentence)
            }

            is LearningAction.OnScroll -> {
                _state.update {
                    it.copy(
                        progress = action.skipTo
                    )
                }
            }

            is LearningAction.OnNext -> {
                _state.update {
                    it.copy(
                        progress = if (it.sentenceItems.size - 1 > it.progress) it.progress + 1 else it.progress
                    )
                }
            }

            is LearningAction.OnTextChange -> {
                _state.update {
                    val inputs = it.sentenceItems.toMutableList().apply {
                        this[it.progress] = this[it.progress].copy(userInput = action.text)
                    }
                    it.copy(
                        sentenceItems = inputs
                    )
                }
            }

            is LearningAction.OnSubmit -> {
                val progress = state.value.progress
                val item = state.value.sentenceItems[progress]
                submitAnswer(
                    progress = progress,
                    mode = state.value.mode,
                    origin = item.sentence,
                    item.userInput
                )
            }

            is LearningAction.OnDoneClick -> {
                println("update Word Learning state")
            }

            else -> Unit
        }
    }

    private fun bookMark(sentence: String) {
        viewModelScope.launch {
            repository.saveBookMark(
                BookMarkRequestQuery(sentence = sentence)
            )
                .onSuccess {
                    _state.update {
                        it.copy(isLoading = false)
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

    private fun getSentences() {
        viewModelScope.launch {
            repository.getSentences(
                SentencesRequestQuery(
                    word = word,
                    difficulty = Difficulty.ADVANCED
                )
            )
                .onSuccess { result ->
                    _state.update {
                        val list = result.map { LearningSentenceItem(sentence = it) }
                        it.copy(
                            sentenceItems = list,
                            isLoading = false
                        )
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            errorMessage = e.toUiText(),
                            isLoading = false
                        )
                    }
                    println(e.name)
                }
        }
    }

    fun submitAnswer(progress: Int, mode: Mode, origin: String, userInput: String) {
        viewModelScope.launch {
            repository.checkAnswer(
                AnswerCheckRequest(
                    origin = origin,
                    mode = mode,
                    userAnswer = userInput
                )
            )
                .onSuccess { result ->
                    _state.update {
                        val updatedItems = it.sentenceItems.toMutableList().apply {
                            this[progress] = this[progress].copy(isCorrect = result.isCorrect)
                        }
                        it.copy(
                            sentenceItems = updatedItems,
                            isLoading = false
                        )
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            errorMessage = e.toUiText(),
                            isLoading = false
                        )
                    }
                }
        }
    }
}