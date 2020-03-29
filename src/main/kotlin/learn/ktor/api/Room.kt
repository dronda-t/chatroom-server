package learn.ktor.api

import learn.ktor.data.entities.RoomEntity

data class Room(val roomKey: String) {
    constructor(roomEntity: RoomEntity) : this(roomEntity.roomKey)
}
