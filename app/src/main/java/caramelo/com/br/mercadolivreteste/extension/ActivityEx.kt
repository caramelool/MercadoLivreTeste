package caramelo.com.br.mercadolivreteste.extension

import android.app.Activity

@Suppress("UNCHECKED_CAST")
fun <T: Any> Activity.extra(name: String): Lazy<T> = lazy {
    intent?.extras?.get(name) as? T ?: throw IllegalStateException()
}