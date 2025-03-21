package org.hyun.projectkmp.word.data.network

import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result
import org.hyun.projectkmp.word.data.dto.SentenceDto
import org.hyun.projectkmp.word.data.dto.WordDto
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.model.WordRequestQuery

interface RemoteWordDataSource {

    suspend fun getTodaysWord(
        query: WordRequestQuery
    ): Result<WordDto, DataError.Remote>

    suspend fun getNewWord(
        query: WordRequestQuery
    ): Result<WordDto, DataError.Remote>

    suspend fun getSentences(
        query: SentencesRequestQuery
    ): Result<SentenceDto, DataError.Remote>
}