package org.hyun.projectkmp

import java.util.concurrent.atomic.AtomicBoolean
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.concurrent.thread

class DesktopVoiceRecognizer: VoiceRecognizer {
    var targetLine: TargetDataLine? = null
    val isRecording = AtomicBoolean(false)
    var recordingThread: Thread? = null

    override fun startRecognition(onData: (ByteArray) -> Unit, onFinished: () -> Unit) {
        if (isRecording.get()) return

        val audioFormat = AudioFormat(
            16000f, // Sample rate
            16,     // Sample size in bits
            1,      // Channels (mono)
            true,   // Signed
            false   // Little endian
        )

        val info = DataLine.Info(TargetDataLine::class.java, audioFormat)
        if (!AudioSystem.isLineSupported(info)) {
            throw IllegalStateException("Line not supported")
        }

        targetLine = AudioSystem.getLine(info) as TargetDataLine
        targetLine?.open(audioFormat)
        targetLine?.start()

        isRecording.set(true)

        recordingThread = thread(start = true) {
            val buffer = ByteArray(4096)
            try {
                while (isRecording.get()) {
                    val bytesRead = targetLine?.read(buffer, 0, buffer.size) ?: break
                    if (bytesRead > 0) {
                        onData(buffer.copyOfRange(0, bytesRead))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stopInternal()
                onFinished()
            }
        }
    }

    override fun stopRecognition() {
        isRecording.set(false)
    }

    fun stopInternal() {
        try {
            targetLine?.stop()
            targetLine?.close()
            targetLine = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

lateinit var voiceRecognizerInstance: VoiceRecognizer
actual fun getVoiceRecognizer(): VoiceRecognizer = voiceRecognizerInstance