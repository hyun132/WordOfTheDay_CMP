package org.hyun.projectkmp.word.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.hyun.projectkmp.word.data.dao.Profile
import org.hyun.projectkmp.word.data.dao.Word

@Database(entities = [Word::class,Profile::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao // DAO 인터페이스
}