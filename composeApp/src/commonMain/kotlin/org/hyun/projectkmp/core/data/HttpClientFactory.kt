package org.hyun.projectkmp.core.data

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.hyun.projectkmp.auth.domain.dto.LoginResponse

object HttpClientFactory {

//    const val BASE_URL = "http://10.0.2.2:8080/api"
    const val BASE_URL = "https://hyun-wordoftheday.duckdns.org/api"
    //private const val BASE_URL = "http://211.209.109.153:8080/api"
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true // 키 없어도 오류 발생 안시킴
                    }
                )
            }
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            Settings().getString("access", "default"),
                            Settings().getString("refresh", "")
                        )
                    }
                    refreshTokens {
                        try {
                            val refreshToken = Settings().getString("refresh", "")

                            val responseStr = client.post("${BASE_URL}/api/auth/refresh") {
                                header("Authorization", "Bearer $refreshToken")
                            }.bodyAsText()

                            if (response.status.isSuccess()) {
                                val tokenPair = Json.decodeFromString<LoginResponse>(responseStr)
                                val newAccess = tokenPair.accessToken
                                val newRefresh = tokenPair.refreshToken

                                // 2-2. 새로운 토큰 저장
                                Settings().putString("access", newAccess)
                                Settings().putString("refresh", newRefresh)

                                // 2-3. 새 토큰 리턴
                                BearerTokens(newAccess, newRefresh)
                            } else {
                                BearerTokens(
                                    Settings().getString("access", ""),
                                    Settings().getString("refresh", "")
                                )
                            }
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            }
        }
    }
}