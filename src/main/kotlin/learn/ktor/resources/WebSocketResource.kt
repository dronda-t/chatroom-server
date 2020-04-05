package learn.ktor.resources

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.application.Application
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import learn.ktor.Session
import learn.ktor.services.ChatService
import learn.ktor.services.RoomService

@ExperimentalCoroutinesApi
@Singleton
class WebSocketResource @Inject constructor(application: Application, roomService: RoomService, chatService: ChatService) {
    init {
        application.routing {
            webSocket("/ws") {
                val session = call.sessions.get<Session>()

                if (session == null) {
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
                    return@webSocket
                }

                val name = call.request.queryParameters["name"]
                val roomKey = call.request.queryParameters["roomKey"]
                requireNotNull(name)
                requireNotNull(roomKey)
                roomService.withTransaction {
                    newUserJoinIfAbsent(name, roomKey, session.id)
                }

                chatService.memberJoin(session.id, this)

                try {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            chatService.receivedMessage(session.id, frame.readText())
                        }
                    }
                } finally {
                    chatService.memberLeft(session.id, this)
                }
            }

        }
    }
}