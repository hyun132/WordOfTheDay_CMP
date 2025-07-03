package org.hyun.projectkmp.auth.presentation.reset_password

sealed interface ResetPasswordAction {
    data object OnSendEmailClick : ResetPasswordAction
    data object OnVerifyClick : ResetPasswordAction
    data object OnSaveClick : ResetPasswordAction
    data object OnBackClick : ResetPasswordAction
    data class OnEmailChange(val email: String) : ResetPasswordAction
    data class OnCodeChange(val code: String) : ResetPasswordAction
    data class OnPasswordChange(val password: String) : ResetPasswordAction
    data class OnConfirmPasswordChange(val password: String) : ResetPasswordAction
}