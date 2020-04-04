package learn.ktor.data.entities

import learn.ktor.data.models.Rooms
import learn.ktor.data.models.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RoomEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoomEntity>(Rooms)

    var roomKey by Rooms.roomKey
    val users by UserEntity referrersOn Users.room
}