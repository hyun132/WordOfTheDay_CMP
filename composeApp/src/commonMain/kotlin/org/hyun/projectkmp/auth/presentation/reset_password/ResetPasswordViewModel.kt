package org.hyun.projectkmp.auth.presentation.reset_password

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
import org.hyun.projectkmp.auth.domain.dto.request.ResetPasswordRequest
import org.hyun.projectkmp.auth.domain.dto.request.SendCodeRequest
import org.hyun.projectkmp.auth.domain.dto.request.VerifyCodeRequest
import org.hyun.projectkmp.auth.domain.repository.RemoteRepository
import org.hyun.projectkmp.core.domain.onError
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.core.presentation.UiEffect
import org.hyun.projectkmp.word.domain.repository.WordRepository

class ResetPasswordViewModel(
    val authRepository: RemoteRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state

    private val _effect = MutableSharedFlow<UiEffect>()
    val effect: SharedFlow<UiEffect> = _effect.asSharedFlow()

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
            }

            is ResetPasswordAction.OnCodeChange -> {
                _state.update { it.copy(code = action.code) }
            }

            is ResetPasswordAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
            }

            is ResetPasswordAction.OnConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = action.password) }
            }

            is ResetPasswordAction.OnSendEmailClick -> {
                requestCode(state.value.email)
            }

            ResetPasswordAction.OnSaveClick -> {
                resetPassword(state.value.email,state.value.password,state.value.confirmPassword)
            }

            ResetPasswordAction.OnVerifyClick -> {
                verifyCode(state.value.email, state.value.code)
            }

            else -> {}
        }
    }

    fun requestCode(email: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.requestSendCode(SendCodeRequest(email = email))
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isCodeRequested = true) }
                }
                .onError { e ->
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(UiEffect.ShowError(e.name))
                }
        }
    }

    fun verifyCode(email: String, code: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.verifyCode(VerifyCodeRequest(email = email, code = code))
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isEmailVerified = true) }
                }
                .onError { e ->
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(UiEffect.ShowError(e.name))
                }
        }
    }

    fun resetPassword(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (password != confirmPassword) {
                _effect.emit(UiEffect.ShowError("비밀번호가 일치하지 않습니다."))
                return@launch
            }
            _state.update { it.copy(isLoading = true) }
            authRepository.resetPassword(
                ResetPasswordRequest(
                    email = email,
                    newPassword = password
                )
            ).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.NavigateTo(Routes.Login))
            }.onError { e->
                _state.update { it.copy(isLoading = false) }
                _effect.emit(UiEffect.ShowError(e.name))
            }
        }
    }
}
