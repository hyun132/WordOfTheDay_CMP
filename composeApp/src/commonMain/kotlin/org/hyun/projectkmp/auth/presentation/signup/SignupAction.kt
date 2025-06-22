package org.hyun.projectkmp.auth.presentation.signup

sealed interface SignupAction {
    data object OnBackPress:SignupAction
    data object TogglePasswordVisibility:SignupAction
    data object TogglePasswordConfirmVisibility:SignupAction
    data object OnSignupClick:SignupAction
    data class OnEmailChange(val email:String):SignupAction
    data class OnPasswordChange(val password:String):SignupAction
    data class OnPasswordConfirmChange(val password:String):SignupAction
}