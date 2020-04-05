package learn.ktor.exceptions

import java.lang.RuntimeException

class UnprocessableEntityException(message: String?) : RuntimeException(message)
