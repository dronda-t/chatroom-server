package learn.ktor.data.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Users : IntIdTable() {
    val name: Column<String> = varchar("name", 255)
    val sessionKey: Column<String> = varchar("session_key", 255)
    val room = reference("room_id", Rooms)
}