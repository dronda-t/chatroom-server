package learn.ktor.lib.shortId

import java.security.SecureRandom
import java.util.*

object ShortId {
    private val random: SecureRandom = SecureRandom()
    private val encoder: Base64.Encoder = Base64.getUrlEncoder().withoutPadding()

    fun generate(): String {
        val buffer = ByteArray(20)
        random.nextBytes(buffer)
        return encoder.encodeToString(buffer)
    }
}