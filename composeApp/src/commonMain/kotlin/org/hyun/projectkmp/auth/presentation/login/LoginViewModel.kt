package org.hyun.projectkmp.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.auth.domain.dto.LoginRequest
import org.hyun.projectkmp.auth.domain.repository.RemoteRepository
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.UiEffect
import org.hyun.projectkmp.core.presentation.UiText
import wordoftheday.composeapp.generated.resources.Res
import wordoftheday.composeapp.generated.resources.login_failed

class LoginViewModel(
    private val repository: RemoteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect

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

    fun login() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val state = state.value
            repository.login(
                LoginRequest(
                    email = state.email,
                    password = state.password
                )
            ).onSuccess {
                _state.update { it.copy(isLoggedIn = true, isLoading = false) }
            }.onError { e ->
                _effect.tryEmit(UiEffect.ShowError(e.name))
                _state.update { it.copy(isLoading = false, email = e.name) }
            }
        }
    }

}