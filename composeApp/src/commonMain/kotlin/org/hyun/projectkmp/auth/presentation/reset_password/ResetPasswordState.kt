package org.hyun.projectkmp.auth.presentation.reset_password

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val email: String = "",
    val isCodeRequested: Boolean = false,
    val code: String = "",
    val isEmailVerified: Boolean = false,
    val password: String = "",
    val confirmPassword: String = ""
)