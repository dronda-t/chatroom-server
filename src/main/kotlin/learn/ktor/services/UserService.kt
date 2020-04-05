package learn.ktor.services

import learn.ktor.data.entities.RoomEntity
import learn.ktor.data.entities.UserEntity
import learn.ktor.data.models.Users

class UserService {
    fun getUserBy(sessionKey: String) = UserEntity.find { Users.sessionKey eq sessionKey }.firstOrNull()

    fun createUser(name: String, sessionKey: String, roomEntity: RoomEntity) = UserEntity.new {
        this.name = name
        this.sessionKey = sessionKey
        this.room = roomEntity
    }
}