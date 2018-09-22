package caramelo.com.br.mercadolivreteste.ui.payment

import android.arch.lifecycle.*
import android.support.annotation.VisibleForTesting
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.extension.addSource
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.model.PaymentMethod
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodState as State

class PaymentMethodViewModel(
        val payment: Payment,
        private val repository: PaymentRepository
) : BaseViewModel() {

    @VisibleForTesting val loadingState = MutableLiveData<State>()
    @VisibleForTesting val buttonState = MutableLiveData<State>()
    @VisibleForTesting val listState = MutableLiveData<State>()

    val state: MediatorLiveData<State>
        get() = MediatorLiveData<State>().apply {
            addSource(loadingState)
            addSource(buttonState)
            addSource(listState)
        }

    val paymentMethodList = mutableListOf<PaymentMethod>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        if (listState.value == null) {
            requestPaymentMethods()
        }
        if (buttonState.value == null) {
            disableNextButton()
        }
    }

    fun requestPaymentMethods() {
        runJob {
            showLoading()
            try {
                paymentMethodList.apply {
                    clear()
                    addAll(repository.creditCardPaymentMethods())
                }
                showPaymentMethods()
            } catch (e: RequestException) {
                showError()
            }
            hideLoading()
        }
    }

    fun setPaymentMethod(id: String) {
        val method = paymentMethodList.find { it.id == id }
        payment.paymentMethod = method
        enableNextButton()
    }

    private fun showLoading() {
        loadingState.postValue(State.Loading(true))
    }

    private fun hideLoading() {
        loadingState.postValue(State.Loading(false))
    }

    private fun enableNextButton() {
        buttonState.postValue(State.Changes.NextButton(true))
    }

    private fun disableNextButton() {
        buttonState.postValue(State.Changes.NextButton(false))
    }

    private fun showPaymentMethods() {
        listState.postValue(State.Received.PaymentMethods(paymentMethodList))
    }

    private fun showError() {
        listState.postValue(State.Error())
    }
}

sealed class PaymentMethodState {

    data class Loading(val loading: Boolean) : State()

    sealed class Changes : State() {
        data class NextButton(val enable: Boolean) : Changes()
    }

    class Error : State()

    sealed class Received : State() {
        data class PaymentMethods(val list: List<PaymentMethod>) : Received()
    }
}