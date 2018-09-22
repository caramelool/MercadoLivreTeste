package caramelo.com.br.mercadolivreteste.ui.base

import android.arch.lifecycle.*
import android.support.annotation.VisibleForTesting
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext

abstract class BaseViewModel: ViewModel(), LifecycleObserver {

    private var jobContext: CoroutineContext = UI

    private val jobList = mutableListOf<Job>()

    fun runJob(block: suspend CoroutineScope.() -> Unit) {
        val job = launch(jobContext) {
            block()
        }
        jobList.add(job)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun clearJobs() {
        jobList.forEach { it.cancel() }
        jobList.clear()
    }

    @VisibleForTesting
    fun enableUnitTest() {
        jobContext = Unconfined
    }

    class Factory<T : ViewModel>(private val viewModel: () -> T): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return viewModel() as T
        }
    }
}