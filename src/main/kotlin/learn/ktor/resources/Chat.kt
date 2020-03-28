package learn.ktor.resources

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.post
import learn.ktor.data.DatabaseConnection.getDatabase
import learn.ktor.data.entities.Room
import learn.ktor.lib.shortId.ShortId
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.chat() {
    post("/createRoom") {
        transaction(getDatabase(application)) {
            Room.new { roomKey = ShortId.generate() }
        }

        call.respondText("Success")
    }
}
