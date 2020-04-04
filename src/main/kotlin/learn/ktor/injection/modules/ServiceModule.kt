package learn.ktor.injection.modules

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import learn.ktor.services.ChatService
import learn.ktor.services.RoomService

class ServiceModule : AbstractModule() {
    override fun configure() {
        bind(ChatService::class.java).`in`(Singleton::class.java)
        bind(RoomService::class.java).`in`(Singleton::class.java)
    }
}