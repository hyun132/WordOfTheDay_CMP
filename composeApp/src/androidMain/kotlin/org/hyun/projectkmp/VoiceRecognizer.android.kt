package org.hyun.projectkmp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class AndroidVoiceRecognizer(private val context: Context) : VoiceRecognizer {
    private var recognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var isActive = false

    override fun startRecognition(onText: (String) -> Unit, onFinished: () -> Unit) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000)
            putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                3000
            )
        }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle) {
                val text =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                text?.let(onText)
                onFinished()
            }

            override fun onReadyForSpeech(params: Bundle?) {

            }

            override fun onBeginningOfSpeech() {
            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(error: Int) {
                onFinished()
            }

            override fun onPartialResults(partialResults: Bundle) {
                val text = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                text?.let(onText)
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }
        })

        recognizer.startListening(intent)
        isActive = true
    }

    override fun stopRecognition() {
        recognizer.stopListening()
        isActive = false
    }

    override fun isRecognizing(): Boolean = isActive
}

lateinit var voiceRecognizerInstance: VoiceRecognizer
actual fun getVoiceRecognizer(): VoiceRecognizer = voiceRecognizerInstance