package org.hyun.projectkmp.word.data.repository

import kotlinx.coroutines.flow.firstOrNull
import org.hyun.projectkmp.core.domain.DataError
import org.hyun.projectkmp.core.domain.Result
import org.hyun.projectkmp.core.domain.map
import org.hyun.projectkmp.core.domain.onSuccess
import org.hyun.projectkmp.utils.getFormattedCurrentDate
import org.hyun.projectkmp.word.data.dao.Profile
import org.hyun.projectkmp.word.data.local.WordDao
import org.hyun.projectkmp.word.data.mapper.toLearningHistory
import org.hyun.projectkmp.word.data.mapper.toSentence
import org.hyun.projectkmp.word.data.mapper.toWord
import org.hyun.projectkmp.word.data.network.RemoteWordDataSource
import org.hyun.projectkmp.word.domain.LearningHistories
import org.hyun.projectkmp.word.domain.Sentence
import org.hyun.projectkmp.word.domain.Sentences
import org.hyun.projectkmp.word.domain.Word
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
import org.hyun.projectkmp.word.domain.repository.WordRepository

class DefaultWordRepository(
    private val remoteWordDataSource: RemoteWordDataSource,
    private val wordDao: WordDao
) : WordRepository {
    override suspend fun getTodaysWord(requestQuery: WordRequestQuery): Result<Word, DataError.Remote> {
        val date = getFormattedCurrentDate()
        val word = wordDao.getTodaysWord(date)
        if (word != null) return Result.Success(
            data = Word(
                word = word.word,
                meaning = word.meaning
            )
        )

        return remoteWordDataSource
            .getTodaysWord(requestQuery)
            .map { it.toWord() }
            .onSuccess {
                wordDao.wordUpsert(org.hyun.projectkmp.word.data.dao.Word(it.word, it.meaning, date))
                it
            }
    }

    override suspend fun getNewWord(requestQuery: WordRequestQuery): Result<Word, DataError.Remote> {

        val date = getFormattedCurrentDate()

        return remoteWordDataSource
            .getNewWord(requestQuery)
            .map { it.toWord() }
            .onSuccess {
                wordDao.wordUpsert(org.hyun.projectkmp.word.data.dao.Word(it.word, it.meaning, date))
                it
            }
    }

    override suspend fun getSentences(requestQuery: SentencesRequestQuery): Result<List<String>, DataError.Remote> {
        return remoteWordDataSource.getSentences(requestQuery)
            .map { it.sentences?.map { it.sentence ?: "" } ?: emptyList() }
    }

    override suspend fun saveBookMark(requestQuery: BookMarkRequestQuery): Result<String, DataError.Remote> {
        return remoteWordDataSource.saveBookmark(requestQuery).map { it.sentence ?: "" }
    }

    override suspend fun getBookMarks(requestQuery: BookMarksRequestQuery): Result<Sentences, DataError.Remote> {
        return remoteWordDataSource.getBookmarks(requestQuery)
            .map { Sentences(sentences = it.sentences?.map { it.toSentence() } ?: emptyList()) }
    }

    override suspend fun deleteBookMark(requestQuery: BookMarkRequestQuery): Result<Sentence, DataError.Remote> {
        return remoteWordDataSource.deleteBookmark(requestQuery).map { it.toSentence() }
    }

    override suspend fun getLearnedWordCount(): Result<Long, DataError.Remote> {
        return remoteWordDataSource.getLearnedWordCount()
    }

    override suspend fun checkAnswer(requestQuery: AnswerCheckRequest): Result<AnswerResult, DataError.Remote> {
        return remoteWordDataSource.checkAnswer(requestQuery)
    }

    override suspend fun saveLearningHistory(learningCompleteRequest: LearningCompleteRequest): Result<LearningCompleteResponse, DataError.Remote> {
        return remoteWordDataSource.saveLearningHistory(learningCompleteRequest)
    }

    override suspend fun getLearningHistories(learningCompleteRequest: LearningHistoriesRequest): Result<LearningHistories, DataError.Remote> {
        return remoteWordDataSource.getLearningHistories(learningCompleteRequest).map { response ->
            val map =
                response.learningHistories.map { it.toLearningHistory() }.groupBy { it.learnedAt }
            LearningHistories(
                learningHistories = map,
                yearMonth = learningCompleteRequest.yearMonth
            )
        }
    }

    override suspend fun createProfile(request: CreateProfileRequest): Result<CreateProfileResponse, DataError.Remote> {
        return remoteWordDataSource.createProfile(request).onSuccess {
            wordDao.profileUpsert(Profile(it.username, it.difficulty, it.topic, it.createdAt))
            it
        }
    }

    override suspend fun getProfile(): Result<ProfileResponse, DataError.Remote> {
        val profile = wordDao.getProfile()
        if (profile != null) return Result.Success(
            data = ProfileResponse(
                profile.username,
                profile.difficulty,
                profile.topic,
                profile.createdAt
            )
        )
        return remoteWordDataSource.getProfile().onSuccess {
            wordDao.profileUpsert(Profile(it.username, it.difficulty, it.topic, it.createdAt))
            it
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<ProfileResponse, DataError.Remote> {
        return remoteWordDataSource.updateProfile(request).onSuccess {
            wordDao.profileUpsert(Profile(it.username, it.difficulty, it.topic, it.createdAt))
            it
        }
    }
}