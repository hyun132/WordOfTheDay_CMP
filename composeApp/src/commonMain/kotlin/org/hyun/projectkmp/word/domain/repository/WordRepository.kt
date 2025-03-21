package org.hyun.projectkmp.word.domain.repository

import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.Word
import org.hyun.projectkmp.word.domain.model.WordRequestQuery

interface WordRepository {
    suspend fun getTodaysWord(requestQuery: WordRequestQuery):Result<Word,DataError.Remote>
    suspend fun getNewWord(requestQuery: WordRequestQuery):Result<Word,DataError.Remote>
    suspend fun getSentences(requestQuery: SentencesRequestQuery):Result<List<String>,DataError.Remote>
}