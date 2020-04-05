package learn.ktor.services

import learn.ktor.data.entities.RoomEntity
import learn.ktor.data.entities.UserEntity
import learn.ktor.data.models.Users
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.with
import kotlin.reflect.KProperty1

class UserService {
    fun getUserBy(sessionKey: String, vararg relations: KProperty1<out Entity<*>, Any?>) =
            UserEntity.find { Users.sessionKey eq sessionKey }.with(*relations).firstOrNull()

    fun createUser(name: String, sessionKey: String, roomEntity: RoomEntity) = UserEntity.new {
        this.name = name
        this.sessionKey = sessionKey
        this.room = roomEntity
    }

    fun deleteBy(sessionKey: String) {
        UserEntity.find { Users.sessionKey eq sessionKey }.firstOrNull()?.delete()
    }
}