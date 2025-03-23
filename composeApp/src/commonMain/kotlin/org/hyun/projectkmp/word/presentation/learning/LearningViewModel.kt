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
import org.hyun.projectkmp.core.presentation.UiText
import org.hyun.projectkmp.core.presentation.toUiText
import org.hyun.projectkmp.word.domain.Difficulty
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

            else -> Unit
        }
    }

    fun bookMark(sentence: String) {
        println("$sentence is bookmarked")
    }

    fun getSentences() {
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