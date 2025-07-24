package org.hyun.projectkmp.word

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class SttWebSocketClient {
    private var client : HttpClient? = null
    private var session: WebSocketSession? = null

    suspend fun connect(onTextReceived: (String) -> Unit) {
        try {
            client = HttpClient(CIO) {
                install(WebSockets)
                install(HttpTimeout) {
                    connectTimeoutMillis = 5000
                    requestTimeoutMillis = 15000
                }
            }
        session = client?.webSocketSession {

            url("ws://192.168.35.113:8080/ws/stt")
            header("Authorization", "Bearer ${Settings().getString("access","")}")
        }

        listenIncoming(onTextReceived)
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    private fun listenIncoming(onTextReceived: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            session?.incoming?.consumeEach { frame ->
                if (frame is Frame.Text) {
                    onTextReceived(frame.readText())
                }
            }
        }
    }

    fun sendStopMessage(){
        print("sendStopMessage")
        CoroutineScope(Dispatchers.IO).launch {
            session?.send(Frame.Text("STOP"))
        }
    }

    suspend fun sendAudio(data: ByteArray) {
        session?.send(Frame.Binary(true, data))
    }

    suspend fun close() {
        session?.close()
        client?.close()
    }
}