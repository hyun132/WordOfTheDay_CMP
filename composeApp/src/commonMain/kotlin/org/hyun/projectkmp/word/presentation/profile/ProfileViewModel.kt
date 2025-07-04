package org.hyun.projectkmp.word.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.word.domain.Difficulty
import org.hyun.projectkmp.word.domain.repository.WordRepository

class ProfileViewModel(
    private val remoteRepository: WordRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            getProfile()
            getLearnedWordCount()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: ProfileAction) {
        when (action) {
            else -> Unit
        }
    }

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            remoteRepository.getProfile()
                .onSuccess { response ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            difficulty = Difficulty.valueOf(response.difficulty),
                            topic = response.topic,
                            username = response.username
                        )
                    }
                }
                .onError {e ->
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun getLearnedWordCount() {
        viewModelScope.launch {
            remoteRepository.getLearnedWordCount()
                .onSuccess { count ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            learnedWordCount = count.toInt()
                        )
                    }
                }
                .onError { e ->
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
        }
    }
}