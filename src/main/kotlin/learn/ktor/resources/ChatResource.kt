package learn.ktor.resources

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import learn.ktor.api.Room
import learn.ktor.data.DatabaseConnection.getDatabase
import learn.ktor.data.entities.RoomEntity
import learn.ktor.exceptions.UnprocessableEntityException
import learn.ktor.lib.shortId.ShortId
import learn.ktor.services.RoomService
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

@Singleton
class ChatResource @Inject constructor(application: Application, roomService: RoomService) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        application.routing {
            post("/createRoom") {
                val room = transaction(getDatabase(application)) {
                    RoomEntity.new { roomKey = ShortId.generate() }
                }

                call.respond(Room(room))
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
    }
}
