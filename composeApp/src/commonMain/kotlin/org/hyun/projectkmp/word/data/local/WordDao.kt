package org.hyun.projectkmp.word.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.hyun.projectkmp.word.data.dao.Word

@Dao
interface WordDao {

    @Query("SELECT * FROM word_table WHERE learnedAt = :date limit 1")
    fun getTodaysWord(date: String): Flow<Word?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(word: Word)
}