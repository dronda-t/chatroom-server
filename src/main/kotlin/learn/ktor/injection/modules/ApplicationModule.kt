package learn.ktor.injection.modules

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import io.ktor.application.Application

class ApplicationModule(private val application: Application, private val objectMapper: ObjectMapper) : AbstractModule() {
    override fun configure() {
        bind(Application::class.java).toInstance(application)
        bind(ObjectMapper::class.java).toInstance(objectMapper)
    }
}