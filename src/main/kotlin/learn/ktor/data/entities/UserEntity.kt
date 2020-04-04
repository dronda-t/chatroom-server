package learn.ktor.data.entities

import learn.ktor.data.models.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)

    var name by Users.name
    var sessionKey by Users.sessionKey
    var room by RoomEntity referencedOn Users.room
}