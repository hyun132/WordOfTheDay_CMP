package org.hyun.projectkmp.word.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.hyun.projectkmp.core.data.safeCall
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result
import org.hyun.projectkmp.word.data.dto.SentenceDto
import org.hyun.projectkmp.word.data.dto.WordDto
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.model.WordRequestQuery

private const val BASE_URL = "http://10.0.2.2:8080"

class KtorRemoteWordDataSource(
    private val httpClient: HttpClient
) : RemoteWordDataSource {
    override suspend fun getTodaysWord(query: WordRequestQuery): Result<WordDto, DataError.Remote> {
        return safeCall {
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

    override suspend fun getSentences(query: SentencesRequestQuery): Result<SentenceDto, DataError.Remote> {
        return safeCall {
            httpClient.get("$BASE_URL/sentence") {
                parameter("word", query.word)
                parameter("difficulty", query.difficulty.name)
            }
        }
    }
}