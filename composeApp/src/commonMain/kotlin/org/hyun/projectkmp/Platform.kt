package org.hyun.projectkmp

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.hyun.projectkmp.word.data.local.WordRoomDatabase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect class AudioPermissionManager {
    suspend fun requestPermission(): Boolean
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object WordDatabaseConstructor : RoomDatabaseConstructor<WordRoomDatabase> {
    override fun initialize(): WordRoomDatabase
}

fun getWordDatabase(builder: RoomDatabase.Builder<WordRoomDatabase>): WordRoomDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}