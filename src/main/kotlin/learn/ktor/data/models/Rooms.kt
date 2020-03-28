package learn.ktor.data.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Rooms : IntIdTable() {
    val roomId: Column<String> = varchar("room_key", 40)
}