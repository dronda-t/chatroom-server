package learn.ktor.injection.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database

class ApplicationModule(private val application: Application, private val objectMapper: ObjectMapper) : AbstractModule() {
    override fun configure() {
        bind(Application::class.java).toInstance(application)
        bind(ObjectMapper::class.java).toInstance(objectMapper)
    }

    @KtorExperimentalAPI
    @Provides
    @Singleton
    fun provideDatabase(): Database {
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