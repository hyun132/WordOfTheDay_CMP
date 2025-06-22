package org.hyun.projectkmp.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyun.projectkmp.app.Routes
import org.hyun.projectkmp.auth.domain.dto.SignupRequest
import org.hyun.projectkmp.auth.domain.repository.RemoteRepository
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.UiEffect

class SignupViewModel(
    val repository: RemoteRepository
) : ViewModel() {

    val _state: MutableStateFlow<SignupState> = MutableStateFlow(SignupState())
    val state: StateFlow<SignupState> = _state

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onAction(action: SignupAction) {
        when (action) {
            is SignupAction.TogglePasswordVisibility -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }

            is SignupAction.TogglePasswordConfirmVisibility -> {
                _state.update { it.copy(showConfirmPassword = !it.showConfirmPassword) }
            }

            is SignupAction.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
            }

            is SignupAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
            }

            is SignupAction.OnPasswordConfirmChange -> {
                _state.update {
                    it.copy(
                        confirmPassword = action.password,
                        isSamePassword = it.password == action.password
                    )
                }
            }

            is SignupAction.OnSignupClick -> {
                checkValidationAndSignup(state.value.email, state.value.password)
            }

            else -> Unit
        }
    }

    fun checkValidationAndSignup(email: String, password: String) {
        val emailStr = email.trim()
        val passwordStr = password.trim()
        if (emailStr.isNotBlank() && emailStr.isNotEmpty() && passwordStr.isNotBlank() && passwordStr.isNotEmpty()&& passwordStr==password) {
            signUp(emailStr, passwordStr)
        } else {
            _effect.tryEmit(UiEffect.ShowError("Passwords do not match."))
        }
    }

    fun signUp(email: String, password: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.signUp(
                SignupRequest(
                    email = email,
                    password = password
                )
            ).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.ShowError("회원가입 완료"))
                _effect.emit(UiEffect.NavigateTo(Routes.Login))
            }.onError { e ->
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.ShowError(e.name))
            }
        }
    }
}