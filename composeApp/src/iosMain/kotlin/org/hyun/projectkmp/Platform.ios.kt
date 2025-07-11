package org.hyun.projectkmp

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import org.hyun.projectkmp.word.data.local.WordRoomDatabase
import platform.AVFAudio.AVAudioSession
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIDevice
import kotlin.coroutines.resume

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual class AudioPermissionManager {
    actual suspend fun requestPermission(): Boolean = suspendCancellableCoroutine { cont ->
        val audioSession = AVAudioSession.sharedInstance()
        audioSession.requestRecordPermission { granted ->
            cont.resume(granted)
        }
    }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<WordRoomDatabase> {
    val dbFilePath = documentDirectory() + "/word_database.db"
    return Room.databaseBuilder<WordRoomDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )

    return requireNotNull(documentDirectory?.path)
}