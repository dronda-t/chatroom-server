package learn.ktor.injection.providers

import com.google.inject.Inject
import com.google.inject.Provider
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database

class ProvideDatabase @Inject constructor(private val application: Application) : Provider<Database> {

    @KtorExperimentalAPI
    @Inject
    override fun get(): Database {
        val url = application.environment.config.property("ktor.database.connectionString").getString()
        val driver = application.environment.config.property("ktor.database.driver").getString()
        val user = application.environment.config.property("ktor.database.username").getString()
        val password = application.environment.config.property("ktor.database.password").getString()

        return Database.connect(
                url = url,
                driver = driver,
                user = user,
                password = password
        )
    }
}