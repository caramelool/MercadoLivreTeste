package caramelo.com.br.mercadolivreteste.extension

import kotlinx.coroutines.experimental.Deferred
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RequestException : Exception {
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String? = null,
                cause: Throwable? = null,
                enableSuppression: Boolean = false,
                writableStackTrace: Boolean = false
    ) : super(message, cause, enableSuppression, writableStackTrace)
}

suspend fun <T> Deferred<T>.request(): T {
    return try {
        await()
    } catch (t: Throwable) {
        when (t) {
            is UnknownHostException,
            is SocketTimeoutException -> throw RequestException(t)
            else -> throw t
        }
    }

}