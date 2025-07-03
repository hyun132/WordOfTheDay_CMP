package org.hyun.projectkmp.auth.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
import org.hyun.projectkmp.core.data.safeCall
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result

class KtorRemoteAuthDataSource(
    val httpClient: HttpClient
) : RemoteAuthDataSource {
    //    val BASE_URL = "http://192.168.35.185:8080/api/auth"
    val BASE_URL = "http://10.0.2.2:8080/api"

    override suspend fun signUp(request: SignupRequest): Result<SignupResponse, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/signup") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun login(request: LoginRequest): Result<LoginResponse, DataError.Remote> {
        return safeCall {
            val authProvider = httpClient.authProvider<BearerAuthProvider>()
            requireNotNull(authProvider)
            authProvider.clearToken()
            httpClient.post("$BASE_URL/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun changePassword(request: ChangePasswordRequest): Result<ChangePasswordResponse, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/update") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun checkEmail(email: String): Result<CheckEmailResponse, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/auth/check-email?email=$email") {
                contentType(ContentType.Application.Json)
            }
        }
    }

    override suspend fun getInfo(): Result<InfoResponse, DataError.Remote> {
        return safeCall {
            val authProvider = httpClient.authProvider<BearerAuthProvider>()
            requireNotNull(authProvider)
            authProvider.clearToken()
            httpClient.get("$BASE_URL/auth/me") {
                contentType(ContentType.Application.Json)
            }
        }
    }

    override suspend fun requestSendCode(request: SendCodeRequest): Result<String, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/email/verify-request") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun verifyCode(request: VerifyCodeRequest): Result<VerifyCodeResponse, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/email/verify") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<String, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/email/password-reset") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }
}