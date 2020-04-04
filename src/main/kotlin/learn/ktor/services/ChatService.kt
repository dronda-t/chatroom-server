package learn.ktor.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import learn.ktor.api.chat.Message
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

@Singleton
class ChatService @Inject constructor(private val objectMapper: ObjectMapper) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val members = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun memberJoin(member: String, socket: WebSocketSession) {
        members.putIfAbsent(member, socket)
        logger.debug("Member \"$member\" joined")
    }

    suspend fun memberLeft(member: String, socket: WebSocketSession) {
        members.remove(member)
        logger.debug("Member \"$member\" left")
    }

    suspend fun receivedMessage(id: String, body: String) {
        when {
            body.startsWith(MESSAGE, true) -> sendMessage(id, body)
        }
    }

    private suspend fun sendMessage(id: String, body: String) {
        val unparsedMessage = body.slice(MESSAGE.length until body.length)
        withContext(Dispatchers.IO) {
            try {
                val message = objectMapper.readValue(unparsedMessage, Message::class.java)
                broadcast(message.data)
            } catch (e: IOException) {
                logger.error("Unable to parse message")
            }
        }

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