package org.hyun.projectkmp

interface VoiceRecognizer {
    fun startRecognition(onText: (String) -> Unit, onFinished: () -> Unit)
    fun stopRecognition()
    fun isRecognizing(): Boolean
}

// ViewModel 등에서 주입받아 사용
expect fun getVoiceRecognizer(): VoiceRecognizer
