package org.hyun.projectkmp.word.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.UiEffect
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

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnEditClick -> {
                viewModelScope.launch {
                    _effect.emit(UiEffect.NavigateTo(Routes.UpdateProfile))
                }
            }
            is ProfileAction.OnBackClick -> {

            }
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
                            username = response.username,
                            signUpDate = response.createdAt
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