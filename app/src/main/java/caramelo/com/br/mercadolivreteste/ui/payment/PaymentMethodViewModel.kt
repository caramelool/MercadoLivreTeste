package caramelo.com.br.mercadolivreteste.ui.payment

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.extension.addSource
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.model.PaymentMethod
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodState as State

class PaymentMethodViewModel(
        val payment: Payment,
        private val repository: PaymentRepository
) : ViewModel() {

    private val loadingState = MutableLiveData<State>()

    private val buttonState = MutableLiveData<State>()
        get() {
            if (field.value == null) {
                field.value = State.Idle()
                disableNextButton()
            }
            return field
        }

    private val listState = MutableLiveData<State>()
        get() {
            if (field.value == null) {
                field.value = State.Idle()
                requestPaymentMethods()
            }
            return field
        }

    val state: MediatorLiveData<State>
        get() = MediatorLiveData<State>().apply {
            addSource(loadingState)
            addSource(buttonState)
            addSource(listState)
        }

    private var paymentMethodList = listOf<PaymentMethod>()

    private fun requestPaymentMethods() {
        launch(UI) {
            showLoading()
            try {
                paymentMethodList = repository.creditCardPaymentMethods()
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
    class Idle : State()

    data class Loading(val loading: Boolean) : State()

    sealed class Changes : State() {
        data class NextButton(val enable: Boolean) : Changes()
    }

    class Error : State()

    sealed class Received : State() {
        data class PaymentMethods(val list: List<PaymentMethod>) : Received()
    }
}