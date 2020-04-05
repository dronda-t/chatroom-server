package learn.ktor.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import learn.ktor.api.chat.Message
import learn.ktor.data.entities.UserEntity
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

@Singleton
class ChatService @Inject constructor(private val objectMapper: ObjectMapper, private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val members = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun memberJoin(sessionId: String, socket: WebSocketSession) {
        members.putIfAbsent(sessionId, socket)
        logger.debug("Member \"$sessionId\" joined")
    }

    suspend fun memberLeft(sessionId: String, socket: WebSocketSession) {
        members.remove(sessionId)
        transaction {
            userService.deleteBy(sessionId)
        }

        logger.debug("Member \"$sessionId\" left")
    }

    suspend fun receivedMessage(id: String, body: String) {
        when {
            body.startsWith(MESSAGE, true) -> sendMessage(id, body)
            else -> logger.debug("Unknown command received")
        }
    }

    private suspend fun sendMessage(id: String, body: String) {
        val unparsedMessage = body.slice(MESSAGE.length until body.length)
        withContext(Dispatchers.IO) {
            try {
                val message = objectMapper.readValue(unparsedMessage, Message::class.java)

                transaction {
                    userService.getUserBy(id, UserEntity::room)?.room?.users?.map {
                        it.sessionKey
                    }
                }?.let { userSessionKeys ->
                    broadcast(message.data, userSessionKeys)
                }
            } catch (e: IOException) {
                logger.error("Unable to parse message")
            } catch (e: NoSuchElementException) {
                logger.error("Unable to find user")
            }
        } ?: logger.warn("Unable to send message")
    }

    private suspend fun broadcast(message: String, userSessionKeys: List<String>) {
        userSessionKeys.forEach {
            members[it]?.send(Frame.Text(message))
        }
    }

    companion object {
        private const val MESSAGE = "message"
    }
}