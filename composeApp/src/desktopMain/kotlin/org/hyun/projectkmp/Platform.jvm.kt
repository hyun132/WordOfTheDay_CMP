package org.hyun.projectkmp

import androidx.room.Room
import androidx.room.RoomDatabase
import org.hyun.projectkmp.word.data.local.WordRoomDatabase
import java.io.File

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual class AudioPermissionManager {
    actual suspend fun requestPermission(): Boolean {
        TODO("Not yet implemented")
    }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<WordRoomDatabase> {
    val appDataPath = System.getProperty("user.home") + "/.wordoftheday"
    File(appDataPath).mkdirs() // 데이터베이스 파일을 저장할 디렉토리가 없으면 생성
    val databaseFilePath = File(appDataPath, "word_database.db").absolutePath

    return Room.databaseBuilder<WordRoomDatabase>(
        name = databaseFilePath,
    )
}