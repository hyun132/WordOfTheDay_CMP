package org.hyun.projectkmp

interface VoiceRecognizer {
    fun startRecognition(onData: (ByteArray) -> Unit, onFinished: () -> Unit)
    fun stopRecognition()
}

// ViewModel 등에서 주입받아 사용
expect fun getVoiceRecognizer(): VoiceRecognizer
