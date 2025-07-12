package org.hyun.projectkmp.word.presentation.profile.update

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
import org.hyun.projectkmp.word.data.repository.LocalRepository
import org.hyun.projectkmp.word.domain.Difficulty
import org.hyun.projectkmp.word.domain.model.CreateProfileRequest
import org.hyun.projectkmp.word.domain.model.UpdateProfileRequest
import org.hyun.projectkmp.word.domain.repository.WordRepository

class UpdateProfileViewModel(
    private val repository: WordRepository,
    private val localRepository: LocalRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UpdateProfileState())
    val state = _state.onStart {
        getProfile()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UpdateProfileState())

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onAction(action: UpdateProfileAction) {
        when (action) {
            is UpdateProfileAction.OnNameChanged -> {
                _state.update { it.copy(name = action.name) }
            }

            is UpdateProfileAction.OnTopicChanged -> {
                _state.update { it.copy(topic = action.topic) }
            }

            is UpdateProfileAction.OnDifficultyChanged -> {
                _state.update { it.copy(difficulty = action.difficulty) }
            }

            is UpdateProfileAction.OnMenuExpendChanged -> {
                _state.update { it.copy(isMenuExpended = !it.isMenuExpended) }
            }

            is UpdateProfileAction.OnUpdateClick -> {
                updateProfile(state.value.name, state.value.topic, state.value.difficulty)
            }

            else -> Unit
        }
    }

    fun updateProfile(name: String, topic: String, difficulty: Difficulty) {

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            if (name.isEmpty() || name.isBlank()) {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.ShowError("이름을 설정해주세요"))
                return@launch
            }
            if (topic.isEmpty() || topic.isBlank()) {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.ShowError("주제를 설정해주세요"))
                return@launch
            }
            repository.updateProfile(
                UpdateProfileRequest(
                    username = name,
                    topic = topic,
                    difficulty = difficulty.name
                )
            ).onSuccess {
                localRepository.saveTopic(topic = it.topic)
                localRepository.saveDifficulty(difficulty = it.difficulty)
                localRepository.saveName(name = it.username)
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.NavigateTo(Routes.Home))
            }
                .onError {
                    _effect.emit(UiEffect.ShowError(it.name))
                }
        }
    }

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProfile()
                .onSuccess { response ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            difficulty = Difficulty.valueOf(response.difficulty),
                            topic = response.topic,
                            name = response.username
                        )
                    }
                }
                .onError { e ->
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}