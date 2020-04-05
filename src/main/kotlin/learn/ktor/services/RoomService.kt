package learn.ktor.services

import com.google.inject.Inject
import com.google.inject.Singleton
import learn.ktor.data.entities.RoomEntity
import learn.ktor.data.entities.UserEntity
import learn.ktor.data.models.Rooms
import learn.ktor.exceptions.UnprocessableEntityException
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

@Singleton
class RoomService @Inject constructor(private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun newUserJoinIfAbsent(name: String, roomKey: String, sessionKey: String): Pair<UserEntity, RoomEntity> {
        val user = userService.getUserBy(sessionKey)?.load(UserEntity::room)

        if (user != null) {
            return Pair(user, changeRoom(user, roomKey))
        }
        val room = findRoomBy(roomKey)

        return Pair(userService.createUser(name, sessionKey, room), room)
    }

    private fun changeRoom(user: UserEntity, roomKey: String): RoomEntity {
        val room = findRoomBy(roomKey)
        user.room = room
        return room
    }

    private fun findRoomBy(roomKey: String):RoomEntity = RoomEntity.find { Rooms.roomKey eq roomKey }.firstOrNull()
            ?: run {
                logger.debug("Can't find room with roomKey $roomKey")
                throw UnprocessableEntityException("No rooms found with roomKey $roomKey")
            }

    inline fun <R> withTransaction(crossinline body: RoomService.() -> R): R = transaction { body() }
}
