package org.hyun.projectkmp.auth.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val isLoggedIn:Boolean = false
)
