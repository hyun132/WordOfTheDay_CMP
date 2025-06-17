package org.hyun.projectkmp.auth.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyun.projectkmp.auth.domain.dto.ChangePasswordRequest
import org.hyun.projectkmp.auth.domain.dto.ChangePasswordResponse
import org.hyun.projectkmp.auth.domain.dto.CheckEmailResponse
import org.hyun.projectkmp.auth.domain.dto.LoginRequest
import org.hyun.projectkmp.auth.domain.dto.LoginResponse
import org.hyun.projectkmp.auth.domain.dto.SignupRequest
import org.hyun.projectkmp.auth.domain.dto.SignupResponse
import org.hyun.projectkmp.core.data.safeCall
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result

class KtorRemoteAuthDataSource(
    val httpClient: HttpClient
) :RemoteAuthDataSource{
//    val BASE_URL = "http://192.168.35.185:8080/api/auth"
    val BASE_URL = "http://10.0.2.2:8080/api"

    override suspend fun signUp(request: SignupRequest): Result<SignupResponse, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/signup"){
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun login(request: LoginRequest): Result<LoginResponse, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/login"){
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun changePassword(request: ChangePasswordRequest): Result<ChangePasswordResponse, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/auth/update"){
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun checkEmail(email: String): Result<CheckEmailResponse, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/auth/check-email?email=$email"){
                contentType(ContentType.Application.Json)
            }
        }
    }
}