package org.hyun.projectkmp.word.data.repository

import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result
import org.hyun.projectkmp.core.domain.map
import org.hyun.projectkmp.word.data.mapper.toWord
import org.hyun.projectkmp.word.data.network.RemoteWordDataSource
import org.hyun.projectkmp.word.domain.Word
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.model.WordRequestQuery
import org.hyun.projectkmp.word.domain.repository.WordRepository

class DefaultWordRepository(
    private val remoteWordDataSource: RemoteWordDataSource
) : WordRepository {
    override suspend fun getTodaysWord(requestQuery: WordRequestQuery): Result<Word, DataError.Remote> {
        return remoteWordDataSource
            .getTodaysWord(requestQuery)
            .map { it.toWord() }
    }

    override suspend fun getNewWord(requestQuery: WordRequestQuery): Result<Word, DataError.Remote> {
        return remoteWordDataSource
            .getNewWord(requestQuery)
            .map { it.toWord() }
    }

    override suspend fun getSentences(requestQuery: SentencesRequestQuery): Result<List<String>, DataError.Remote> {
        return remoteWordDataSource.getSentences(requestQuery)
            .map { it.sentences ?: emptyList() }
    }
}