package org.hyun.projectkmp.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
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
import org.hyun.projectkmp.auth.domain.dto.LoginRequest
import org.hyun.projectkmp.auth.domain.repository.RemoteRepository
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.UiEffect

class LoginViewModel(
    private val repository: RemoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            getUser()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            LoginState()
        )

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    val settings = Settings()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnLoginClick -> {
                print("button click")
                login()
            }

            is LoginAction.TogglePasswordVisibility -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }

            is LoginAction.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
            }

            is LoginAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
            }

            else -> {}
        }
    }

    fun getUser() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMyInfo()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(UiEffect.NavigateTo(Routes.MainGraph))
                }
                .onError {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
        }
    }

    fun login() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val state = state.value
            repository.login(
                LoginRequest(
                    email = state.email,
                    password = state.password
                )
            ).onSuccess {
                settings.putString("access", it.accessToken)
                settings.putString("refresh", it.refreshToken)
                _state.update { it.copy(isLoggedIn = true, isLoading = false) }
                delay(200)
                _effect.emit(UiEffect.NavigateTo(Routes.MainGraph))
            }.onError { e ->
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.ShowError("error : " + e.name))
            }
        }
    }

}