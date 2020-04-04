package learn.ktor.services

import com.google.inject.Singleton
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Singleton
class ChatService {
    private val members = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun memberJoin(member: String, socket: WebSocketSession) {
        members.putIfAbsent(member, socket)
    }

    suspend fun memberLeft(member: String, socket: WebSocketSession) {
        members.remove(member)
    }

    suspend fun receivedMessage(id: String, body: String) {
        when {
            body.startsWith(MESSAGE, true) -> sendMessage(id, body)
        }
    }

    private suspend fun sendMessage(id: String, body: String) {
        val message = body.slice(MESSAGE.length until body.length)

        broadcast(message)
    }

    private suspend fun broadcast(message: String) {
        members.values.forEach { socket ->
            socket.send(Frame.Text(message))
        }
    }

    companion object {
        private const val MESSAGE = "message"
    }
}