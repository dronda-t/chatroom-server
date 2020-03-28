package learn.ktor.resources

import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.Routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.consumeEach
import learn.ktor.Session
import learn.ktor.services.ChatService
import learn.ktor.services.RoomService

fun Routing.webSocketResource(roomService: RoomService, chatService: ChatService) {
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
        val user = roomService.joinRoom(name, roomKey)
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