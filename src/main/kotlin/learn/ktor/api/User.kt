package learn.ktor.api

import learn.ktor.data.entities.UserEntity

data class User(
        val id: Int,
        val name: String,
        val roomKey: String
) {
    constructor(userEntity: UserEntity) : this(
            id = userEntity.id.value,
            name = userEntity.name,
            roomKey = userEntity.room.roomKey
    )
}
