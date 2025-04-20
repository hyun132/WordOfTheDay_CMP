package org.hyun.projectkmp.word.domain.repository

import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result
import org.hyun.projectkmp.word.domain.Sentence
import org.hyun.projectkmp.word.domain.Sentences
import org.hyun.projectkmp.word.domain.model.SentencesRequestQuery
import org.hyun.projectkmp.word.domain.Word
import org.hyun.projectkmp.word.domain.model.AnswerCheckRequest
import org.hyun.projectkmp.word.domain.model.AnswerResult
import org.hyun.projectkmp.word.domain.model.BookMarkRequestQuery
import org.hyun.projectkmp.word.domain.model.BookMarksRequestQuery
import org.hyun.projectkmp.word.domain.model.LearningCompleteRequest
import org.hyun.projectkmp.word.domain.model.LearningCompleteResponse
import org.hyun.projectkmp.word.domain.model.WordRequestQuery

interface WordRepository {
    suspend fun getTodaysWord(requestQuery: WordRequestQuery):Result<Word,DataError.Remote>
    suspend fun getNewWord(requestQuery: WordRequestQuery):Result<Word,DataError.Remote>
    suspend fun getSentences(requestQuery: SentencesRequestQuery):Result<List<String>,DataError.Remote>
    suspend fun saveBookMark(requestQuery: BookMarkRequestQuery):Result<String,DataError.Remote>
    suspend fun deleteBookMark(requestQuery: BookMarkRequestQuery):Result<Sentence,DataError.Remote>
    suspend fun getBookMarks(requestQuery: BookMarksRequestQuery):Result<Sentences,DataError.Remote>
    suspend fun getLearnedWordCount():Result<Long,DataError.Remote>
    suspend fun checkAnswer(requestQuery: AnswerCheckRequest):Result<AnswerResult,DataError.Remote>
    suspend fun saveLearningHistory(learningCompleteRequest: LearningCompleteRequest):Result<LearningCompleteResponse,DataError.Remote>
}