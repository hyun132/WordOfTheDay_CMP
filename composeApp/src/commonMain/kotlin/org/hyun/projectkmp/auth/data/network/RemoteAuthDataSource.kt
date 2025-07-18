package org.hyun.projectkmp.auth.data.network

import org.hyun.projectkmp.auth.domain.dto.ChangePasswordRequest
import org.hyun.projectkmp.auth.domain.dto.ChangePasswordResponse
import org.hyun.projectkmp.auth.domain.dto.CheckEmailResponse
import org.hyun.projectkmp.auth.domain.dto.InfoResponse
import org.hyun.projectkmp.auth.domain.dto.LoginRequest
import org.hyun.projectkmp.auth.domain.dto.LoginResponse
import org.hyun.projectkmp.auth.domain.dto.SignupRequest
import org.hyun.projectkmp.auth.domain.dto.SignupResponse
import org.hyun.projectkmp.auth.domain.dto.request.ResetPasswordRequest
import org.hyun.projectkmp.auth.domain.dto.request.SendCodeRequest
import org.hyun.projectkmp.auth.domain.dto.request.VerifyCodeRequest
import org.hyun.projectkmp.auth.domain.dto.response.VerifyCodeResponse
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result

interface RemoteAuthDataSource {
    suspend fun signUp(
        request: SignupRequest
    ): Result<SignupResponse, DataError.Remote>

    suspend fun login(
        request: LoginRequest
    ): Result<LoginResponse, DataError.Remote>

    suspend fun changePassword(
        request: ChangePasswordRequest
    ): Result<ChangePasswordResponse, DataError.Remote>

    suspend fun checkEmail(
        email: String
    ): Result<CheckEmailResponse, DataError.Remote>

    suspend fun getInfo(): Result<InfoResponse, DataError.Remote>

    suspend fun requestSendCode(
        request: SendCodeRequest
    ): Result<String, DataError.Remote>

    suspend fun verifyCode(
        request: VerifyCodeRequest
    ): Result<VerifyCodeResponse, DataError.Remote>

    suspend fun resetPassword(
        request: ResetPasswordRequest
    ): Result<String, DataError.Remote>
}