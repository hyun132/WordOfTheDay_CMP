package org.hyun.projectkmp.auth.data.reporitory

import org.hyun.projectkmp.auth.data.network.RemoteAuthDataSource
import org.hyun.projectkmp.auth.domain.dto.ChangePasswordRequest
import org.hyun.projectkmp.auth.domain.dto.ChangePasswordResponse
import org.hyun.projectkmp.auth.domain.dto.CheckEmailResponse
import org.hyun.projectkmp.auth.domain.dto.LoginRequest
import org.hyun.projectkmp.auth.domain.dto.LoginResponse
import org.hyun.projectkmp.auth.domain.dto.SignupRequest
import org.hyun.projectkmp.auth.domain.dto.SignupResponse
import org.hyun.projectkmp.auth.domain.repository.RemoteRepository
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result

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

}