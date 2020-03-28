package learn.ktor.resources

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.post
import learn.ktor.data.DatabaseConnection.getDatabase
import learn.ktor.data.entities.RoomEntity
import learn.ktor.exceptions.UnprocessableEntityException
import learn.ktor.lib.shortId.ShortId
import learn.ktor.services.RoomService
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

fun Routing.chat(roomService: RoomService) {
    val logger = LoggerFactory.getLogger(this::class.java)

    post("/createRoom") {
        transaction(getDatabase(application)) {
            RoomEntity.new { roomKey = ShortId.generate() }
        }

        call.respondText("Success")
    }

    post("/joinRoom") {
        try {
            val name = call.request.queryParameters["name"]
            val roomKey = call.request.queryParameters["roomKey"]
            requireNotNull(name)
            requireNotNull(roomKey)

            val user = roomService.joinRoom(name, roomKey)
            call.respond(user)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.UnprocessableEntity)
        } catch (e: UnprocessableEntityException) {
            logger.debug("Can't find room")
            call.respond(HttpStatusCode.UnprocessableEntity)
        }
    }
}
