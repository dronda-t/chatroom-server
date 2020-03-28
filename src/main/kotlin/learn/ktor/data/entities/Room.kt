package learn.ktor.data.entities

import learn.ktor.data.models.Rooms
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Room(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Room>(Rooms)

    var roomKey by Rooms.roomId
}