package org.hyun.projectkmp.word.presentation.bookmark

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
import org.hyun.projectkmp.word.domain.model.BookMarksRequestQuery
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.repository.WordRepository

class BookmarkViewModel(
    private val repository: WordRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarkState())
    val state: StateFlow<BookmarkState> = _state
        .onStart {
            getBookmarks()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = _state.value
        )

    fun onAction(action: BookmarkAction) {
        when (action) {
            is BookmarkAction.OnBookMarkClick -> {
                bookMark(action.sentence)
            }

            else -> Unit
        }
    }

    private fun bookMark(sentence: String) {
        println("$sentence is clicked")
    }

    private fun getBookmarks() {
        viewModelScope.launch {
            repository.getBookMarks(
                BookMarksRequestQuery(
                    amount = 10
                )
            )
                .onSuccess { result ->
                    _state.update {
                        it.copy(
                            sentences = result.sentences.map { it.sentence },
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