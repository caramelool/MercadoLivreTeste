package caramelo.com.br.mercadolivreteste.extension

import kotlinx.coroutines.experimental.Deferred
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RequestException(t: Throwable) : Exception(t)

suspend fun <T> Deferred<T>.request() : T {
    return try {
        await()
    } catch (t: Throwable) {
        when(t) {
            is UnknownHostException,
            is SocketTimeoutException -> throw RequestException(t)
            else -> throw t
        }
    }

}