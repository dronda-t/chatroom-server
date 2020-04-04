package learn.ktor.services

import com.google.inject.Singleton
import learn.ktor.api.User
import learn.ktor.data.entities.RoomEntity
import learn.ktor.data.entities.UserEntity
import learn.ktor.data.models.Rooms
import learn.ktor.exceptions.UnprocessableEntityException
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

@Singleton
class RoomService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Throws(UnprocessableEntityException::class)
    fun joinRoom(name: String, roomKey: String, sessionKey: String) = transaction() {
        val room = RoomEntity.find { Rooms.roomKey eq roomKey }.firstOrNull() ?: throw UnprocessableEntityException()
        logger.debug(room.roomKey)

        val user = UserEntity.new {
            this.name = name
            this.sessionKey = sessionKey
            this.room = room
        }
        return@transaction User(user)
    }
}
