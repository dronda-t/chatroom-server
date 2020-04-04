package learn.ktor.injection.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import io.ktor.application.Application
import learn.ktor.injection.providers.ProvideDatabase
import org.jetbrains.exposed.sql.Database

class ApplicationModule(private val application: Application, private val objectMapper: ObjectMapper) : AbstractModule() {
    override fun configure() {
        bind(Application::class.java).toInstance(application)
        bind(ObjectMapper::class.java).toInstance(objectMapper)
        bind(Database::class.java).toProvider(ProvideDatabase::class.java).asEagerSingleton()
    }
}