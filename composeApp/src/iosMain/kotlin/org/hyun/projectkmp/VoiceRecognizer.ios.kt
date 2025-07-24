package org.hyun.projectkmp

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioInputNode
import platform.Speech.SFSpeechAudioBufferRecognitionRequest
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognizer


class IOSVoiceRecognizer : VoiceRecognizer {
    private var audioEngine: AVAudioEngine? = null
    private var inputNode: AVAudioInputNode? = null

    private var isActive = false

    @OptIn(ExperimentalForeignApi::class)
    override fun startRecognition(onData: (ByteArray) -> Unit, onFinished: () -> Unit) {
        val engine = AVAudioEngine()
        val node = engine.inputNode
        val format = node.inputFormatForBus(0u)

        node.installTapOnBus(
            bus = 0u,
            bufferSize = 1024u,
            format = format
        ) { buffer, _ ->
            val channelData = buffer?.floatChannelData?.get(0) ?: return@installTapOnBus
            val frameLength = buffer.frameLength.toInt()

            // Float → Int16 PCM 변환
            val pcmData = ByteArray(frameLength * 2)
            for (i in 0 until frameLength) {
                val floatVal = channelData[i]
                val intVal = (floatVal * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                pcmData[i * 2] = intVal.toByte()
                pcmData[i * 2 + 1] = (intVal shr 8).toByte()
            }

            onData(pcmData)
        }

        audioEngine = engine
        inputNode = node

        engine.prepare()
        engine.startAndReturnError(null)

        isActive = true
    }

    override fun stopRecognition() {
        inputNode?.removeTapOnBus(0u)
        audioEngine?.stop()
        audioEngine = null
        inputNode = null
        isActive = false
    }
}

actual fun getVoiceRecognizer(): VoiceRecognizer = IOSVoiceRecognizer()
