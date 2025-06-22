package org.hyun.projectkmp.auth.domain.repository

import org.hyun.projectkmp.auth.domain.dto.ChangePasswordRequest
import org.hyun.projectkmp.auth.domain.dto.ChangePasswordResponse
import org.hyun.projectkmp.auth.domain.dto.CheckEmailResponse
import org.hyun.projectkmp.auth.domain.dto.InfoResponse
import org.hyun.projectkmp.auth.domain.dto.LoginRequest
import org.hyun.projectkmp.auth.domain.dto.LoginResponse
import org.hyun.projectkmp.auth.domain.dto.SignupRequest
import org.hyun.projectkmp.auth.domain.dto.SignupResponse
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result

interface RemoteRepository {
    suspend fun checkEmail(email: String): Result<CheckEmailResponse, DataError.Remote>
    suspend fun signUp(request: SignupRequest): Result<SignupResponse, DataError.Remote>
    suspend fun login(request: LoginRequest): Result<LoginResponse, DataError.Remote>
    suspend fun newPassword(request: ChangePasswordRequest): Result<ChangePasswordResponse, DataError.Remote>
    suspend fun getMyInfo(): Result<InfoResponse, DataError.Remote>
}