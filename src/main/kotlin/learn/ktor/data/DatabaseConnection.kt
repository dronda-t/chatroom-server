package learn.ktor.data

import io.ktor.application.Application
import org.jetbrains.exposed.sql.Database

object DatabaseConnection {
    @Volatile
    private var INSTANCE: Database? = null

    fun getDatabase(applicationContext: Application): Database {
        if (INSTANCE == null) {
            synchronized(this) {
                val url = applicationContext.environment.config.property("ktor.database.connectionString").getString()
                val driver = applicationContext.environment.config.property("ktor.database.driver").getString()
                val user = applicationContext.environment.config.property("ktor.database.username").getString()
                val password = applicationContext.environment.config.property("ktor.database.password").getString()

                INSTANCE = Database.connect(
                        url = url,
                        driver = driver,
                        user = user,
                        password = password
                )
            }
        }
        return INSTANCE!!
    }

}