package org.hyun.projectkmp.auth.presentation.login

sealed interface LoginAction {
    data object OnLoginClick:LoginAction
    data object TogglePasswordVisibility:LoginAction
    data object OnSignupClick:LoginAction
    data object OnForgotClick:LoginAction
    data class OnEmailChange(val email:String):LoginAction
    data class OnPasswordChange(val password:String):LoginAction
}