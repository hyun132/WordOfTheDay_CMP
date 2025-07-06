package org.hyun.projectkmp

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFAudio.AVAudioSession
import platform.UIKit.UIDevice
import kotlin.coroutines.resume

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
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