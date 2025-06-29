package org.hyun.projectkmp.word.presentation.profile.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.UiEffect
import org.hyun.projectkmp.word.data.repository.LocalRepository
import org.hyun.projectkmp.word.domain.Difficulty
import org.hyun.projectkmp.word.domain.model.CreateProfileRequest
import org.hyun.projectkmp.word.domain.repository.WordRepository

class CreateProfileViewModel(
    private val repository: WordRepository,
    private val localRepository: LocalRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CreateProfileState())
    val state = _state

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onAction(action: CreateProfileAction) {
        when (action) {
            is CreateProfileAction.OnNameChanged -> {
                _state.update { it.copy(name = action.name) }
            }

            is CreateProfileAction.OnTopicChanged -> {
                _state.update { it.copy(topic = action.topic) }
            }

            is CreateProfileAction.OnDifficultyChanged -> {
                _state.update { it.copy(difficulty = action.difficulty) }
            }

            is CreateProfileAction.OnMenuExpendChanged -> {
                _state.update { it.copy(isMenuExpended = !it.isMenuExpended) }
            }

            is CreateProfileAction.OnCreateClick -> {
                saveProfile(state.value.name, state.value.topic, state.value.difficulty)
            }

            else -> Unit
        }
    }

    fun saveProfile(name: String, topic: String, difficulty: Difficulty) {

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            if(name.isEmpty()||name.isBlank()){
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.ShowError("이름을 설정해주세요"))
                return@launch
            }
            if(topic.isEmpty()||topic.isBlank()){
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.ShowError("주제를 설정해주세요"))
                return@launch
            }
            repository.createProfile(
                CreateProfileRequest(
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

}