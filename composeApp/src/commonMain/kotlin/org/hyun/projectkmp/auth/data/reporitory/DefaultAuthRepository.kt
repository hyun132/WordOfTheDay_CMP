package org.hyun.projectkmp.auth.data.reporitory

import org.hyun.projectkmp.auth.data.network.RemoteAuthDataSource
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
import org.hyun.projectkmp.auth.domain.repository.RemoteRepository
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result
import org.hyun.projectkmp.core.domain.onSuccess

class DefaultAuthRepository(
    private val dataSource: RemoteAuthDataSource
) : RemoteRepository {
    override suspend fun checkEmail(email: String): Result<CheckEmailResponse, DataError.Remote> {
        return dataSource.checkEmail(email)
    }

    override suspend fun signUp(request: SignupRequest): Result<SignupResponse, DataError.Remote> {
        return dataSource.signUp(request)
    }

    override suspend fun login(request: LoginRequest): Result<LoginResponse, DataError.Remote> {
        return dataSource.login(request)
    }

    override suspend fun newPassword(request: ChangePasswordRequest): Result<ChangePasswordResponse, DataError.Remote> {
        return dataSource.changePassword(request)
    }

    override suspend fun getMyInfo(): Result<InfoResponse, DataError.Remote> {
        val response = dataSource.getInfo()
        response.onSuccess {

        }
        return response
    }

    override suspend fun requestSendCode(request: SendCodeRequest): Result<String, DataError.Remote> {
        return dataSource.requestSendCode(request)
    }

    override suspend fun verifyCode(request: VerifyCodeRequest): Result<VerifyCodeResponse, DataError.Remote> {
        return dataSource.verifyCode(request)
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<String, DataError.Remote> {
        return dataSource.resetPassword(request)
    }
}