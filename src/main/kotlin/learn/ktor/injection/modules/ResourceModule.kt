package learn.ktor.injection.modules

import com.google.inject.AbstractModule
import learn.ktor.resources.ChatResource
import learn.ktor.resources.WebSocketResource

class ResourceModule : AbstractModule() {
    override fun configure() {
        bind(ChatResource::class.java).asEagerSingleton()
        bind(WebSocketResource::class.java).asEagerSingleton()
    }
}