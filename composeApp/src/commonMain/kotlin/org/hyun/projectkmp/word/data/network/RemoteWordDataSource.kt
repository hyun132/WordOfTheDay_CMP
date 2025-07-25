package org.hyun.projectkmp.word.data.network

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
import org.hyun.projectkmp.word.domain.model.UpdateProfileRequest
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
    ): Result<SentencesDto, DataError.Remote>

    suspend fun saveBookmark(
        query: BookMarkRequestQuery
    ): Result<SentenceDto, DataError.Remote>

    suspend fun deleteBookmark(
        query: BookMarkRequestQuery
    ): Result<SentenceDto, DataError.Remote>

    suspend fun getBookmarks(
        query: BookMarksRequestQuery
    ): Result<SentencesDto, DataError.Remote>

    suspend fun getLearnedWordCount(): Result<Long, DataError.Remote>

    suspend fun checkAnswer(
        request: AnswerCheckRequest
    ): Result<AnswerResult, DataError.Remote>

    suspend fun saveLearningHistory(
        request: LearningCompleteRequest
    ): Result<LearningCompleteResponse, DataError.Remote>

    suspend fun getLearningHistories(
        request: LearningHistoriesRequest
    ): Result<LearningHistoriesResponse, DataError.Remote>

    suspend fun createProfile(
        request: CreateProfileRequest
    ): Result<CreateProfileResponse, DataError.Remote>

    suspend fun getProfile(): Result<ProfileResponse, DataError.Remote>

    suspend fun updateProfile(
        request: UpdateProfileRequest
    ): Result<ProfileResponse, DataError.Remote>
}