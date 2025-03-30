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

            is LearningAction.OnNext -> {
                println("on next from it.progress")
                _state.update {
                    it.copy(
                        progress = if (it.sentences.size > it.progress) it.progress + 1 else it.progress
                    )
                }
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
                        it.copy(
                            sentences = result,
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
}