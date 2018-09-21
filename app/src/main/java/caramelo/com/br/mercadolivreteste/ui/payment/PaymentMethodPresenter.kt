package caramelo.com.br.mercadolivreteste.ui.payment

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.util.Log
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.base.BasePresenter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class PaymentMethodPresenter @Inject constructor(
        private val view: PaymentMethodView,
        private val repository: PaymentRepository
) : BasePresenter() {

    lateinit var payment: Payment

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun requestPaymentMethods() {
        launch {
            try {
                val list = repository.requestPaymentMethods()

            } catch (e: RequestException) {

            }
        }
    }
}

interface PaymentMethodView