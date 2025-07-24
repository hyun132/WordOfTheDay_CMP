package org.hyun.projectkmp

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AndroidVoiceRecognizer() : VoiceRecognizer {
    private var audioRecord: AudioRecord? = null
    private var recordJob: Job? = null
    @Volatile private var isRecording = false
    var isActive = false

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    override fun startRecognition(onData: (ByteArray) -> Unit, onFinished: () -> Unit) {
        stopRecognition()

        val sampleRate = 16000
        val bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        isRecording = true
        audioRecord?.startRecording()

        recordJob = CoroutineScope(Dispatchers.IO).launch {
            val buffer = ByteArray(bufferSize)
            while (isRecording) {
                val readBytes = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                print("readBytes : $readBytes")
                if (readBytes > 0) {
                    onData(buffer.copyOf(readBytes))
                }
            }

            stopInternal()
            withContext(Dispatchers.Main) {
                onFinished()
            }
        }
    }

    override fun stopRecognition() {
        isRecording = false
        recordJob?.cancel()
        stopInternal()
    }

    private fun stopInternal() {
        try {
            if (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                audioRecord?.stop()
            }
            audioRecord?.release()
        } catch (e: IllegalStateException) {
            Log.w("VoiceRecognizer", "AudioRecord stop failed: ${e.message}")
        }
        audioRecord = null
    }

}

lateinit var voiceRecognizerInstance: VoiceRecognizer
actual fun getVoiceRecognizer(): VoiceRecognizer = voiceRecognizerInstance