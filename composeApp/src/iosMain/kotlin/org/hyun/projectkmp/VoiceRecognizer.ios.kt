package org.hyun.projectkmp

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioEngine
import platform.Speech.SFSpeechAudioBufferRecognitionRequest
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognizer


class IOSVoiceRecognizer : VoiceRecognizer {
    private var speechRecognizer: SFSpeechRecognizer? = SFSpeechRecognizer()
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private val audioEngine = AVAudioEngine()

    private var isActive = false

    @OptIn(ExperimentalForeignApi::class)
    override fun startRecognition(onText: (String) -> Unit, onFinished: () -> Unit) {
        val inputNode = audioEngine.inputNode
        recognitionRequest = SFSpeechAudioBufferRecognitionRequest()

        recognitionTask = speechRecognizer?.recognitionTaskWithRequest(
            request = recognitionRequest!!,
            resultHandler = { result, error ->
                if (result != null) {
                    val text = result.bestTranscription.formattedString
                    onText(text)
                    if (result.isFinal()) {
                        stopRecognition()
                        onFinished()
                    }
                } else if (error != null) {
                    stopRecognition()
                    onFinished()
                }
            }
        )

        val recordingFormat = inputNode.outputFormatForBus(0u)
        inputNode.installTapOnBus(
            bus = 0u,
            bufferSize = 1024u,
            format = recordingFormat
        ) { buffer, _ ->
            if (buffer != null) {
                recognitionRequest?.appendAudioPCMBuffer(buffer)
            }
        }

        audioEngine.prepare()
        audioEngine.startAndReturnError(null)

        isActive = true
    }

    override fun stopRecognition() {
        audioEngine.stop()
        audioEngine.inputNode.removeTapOnBus(0u)
        recognitionRequest?.endAudio()
        recognitionTask?.cancel()
        isActive = false
    }

    override fun isRecognizing(): Boolean = isActive
}

actual fun getVoiceRecognizer(): VoiceRecognizer = IOSVoiceRecognizer()
