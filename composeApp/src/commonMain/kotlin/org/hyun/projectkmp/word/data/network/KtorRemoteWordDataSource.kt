package org.hyun.projectkmp.word.data.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyun.projectkmp.core.data.safeCall
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result
import org.hyun.projectkmp.word.data.dto.LearningHistoriesResponse
import org.hyun.projectkmp.word.data.dto.SentenceDto
import org.hyun.projectkmp.word.data.dto.SentencesDto
import org.hyun.projectkmp.word.data.dto.WordDto
import org.hyun.projectkmp.word.domain.model.AnswerCheckRequest
import org.hyun.projectkmp.word.domain.model.AnswerResult
import org.hyun.projectkmp.word.domain.model.BookMarkRequestQuery
import org.hyun.projectkmp.word.domain.model.BookMarksRequestQuery
import org.hyun.projectkmp.word.domain.model.CreateProfileRequest
import org.hyun.projectkmp.word.domain.model.CreateProfileResponse
import org.hyun.projectkmp.word.domain.model.LearningCompleteRequest
import org.hyun.projectkmp.word.domain.model.LearningCompleteResponse
import org.hyun.projectkmp.word.domain.model.LearningHistoriesRequest
import org.hyun.projectkmp.word.domain.model.ProfileResponse
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.model.WordRequestQuery

private const val BASE_URL = "http://10.0.2.2:8080/api"
//private const val BASE_URL = "http://211.209.109.153:8080/api"

class KtorRemoteWordDataSource(
    private val httpClient: HttpClient
) : RemoteWordDataSource {
    override suspend fun getTodaysWord(query: WordRequestQuery): Result<WordDto, DataError.Remote> {
        return safeCall {
            val authProvider = httpClient.authProvider<BearerAuthProvider>()
            requireNotNull(authProvider)
            authProvider.clearToken()
            httpClient.get("$BASE_URL/word"){
                parameter("subject",query.subject)
                parameter("difficulty",query.difficulty.name)
            }
        }
    }

    override suspend fun getNewWord(query: WordRequestQuery): Result<WordDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/word/new"){
                parameter("subject",query.subject)
                parameter("difficulty",query.difficulty.name)
            }
        }
    }

    override suspend fun getSentences(query: SentencesRequestQuery): Result<SentencesDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/sentence") {
                parameter("word", query.word)
                parameter("difficulty", query.difficulty.name)
            }
        }
    }

    override suspend fun saveBookmark(query: BookMarkRequestQuery): Result<SentenceDto, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/me/bookmark"){
                contentType(ContentType.Application.Json)
                setBody(query)
            }
        }
    }

    override suspend fun deleteBookmark(query: BookMarkRequestQuery): Result<SentenceDto, DataError.Remote> {
        return safeCall {
            httpClient.delete("$BASE_URL/me/bookmark"){
                contentType(ContentType.Application.Json)
                setBody(query)
            }
        }
    }

    override suspend fun getBookmarks(query: BookMarksRequestQuery): Result<SentencesDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/me/bookmark")
        }
    }

    override suspend fun getLearnedWordCount(): Result<Long, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/me/learning-history/count")
        }
    }

    override suspend fun checkAnswer(request: AnswerCheckRequest): Result<AnswerResult, DataError.Remote> {
        return safeCall {
            println("checkAnswer")
            httpClient.post("$BASE_URL/learning/answer"){
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun saveLearningHistory(request: LearningCompleteRequest): Result<LearningCompleteResponse, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/me/learning-history"){
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun getLearningHistories(request: LearningHistoriesRequest): Result<LearningHistoriesResponse, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/me/learning-history"){
                parameter("yearMonth", request.yearMonth)
            }
        }
    }

    override suspend fun createProfile(request: CreateProfileRequest): Result<CreateProfileResponse, DataError.Remote> {
        return safeCall {
            httpClient.post("$BASE_URL/profile"){
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
    }

    override suspend fun getProfile(): Result<ProfileResponse, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/profile")
        }
    }
}