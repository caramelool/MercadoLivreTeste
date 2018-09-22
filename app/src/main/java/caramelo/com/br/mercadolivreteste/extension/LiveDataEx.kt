package caramelo.com.br.mercadolivreteste.extension

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData

fun <T> MediatorLiveData<T>.addSource(source: LiveData<T>) {
    addSource(source) { value = it }
}