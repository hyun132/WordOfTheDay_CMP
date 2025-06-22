package org.hyun.projectkmp.auth.presentation.signup

data class SignupState(
    val isLoading: Boolean = false,
    val email:String = "",
    val isEmailChecked:Boolean = false,
    val password:String = "",
    val confirmPassword:String = "",
    val isSamePassword:Boolean = false,
    val showPassword:Boolean = false,
    val showConfirmPassword:Boolean = false,
)