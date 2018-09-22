package caramelo.com.br.mercadolivreteste.ui.installment

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.extension.addSource
import caramelo.com.br.mercadolivreteste.model.Installment
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel

class InstallmentViewModel(
        val payment: Payment,
        val repository: PaymentRepository
) : BaseViewModel() {

    private val loadingState = MutableLiveData<State>()
    private val payButtonState = MutableLiveData<State>()
    private val amountTextState = MutableLiveData<State>()
    private val listState = MutableLiveData<State>()

    val state = MediatorLiveData<State>().apply {
        addSource(loadingState)
        addSource(payButtonState)
        addSource(amountTextState)
        addSource(listState)
    }

    private lateinit var installment: Installment

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        if (payButtonState.value == null) {
            disablePayButton()
        }

        if (listState.value == null) {
            requestInstallment()
        }
    }

    fun requestInstallment() {
        runJob {
            showLoading()
            try {
                installment = repository.installment(payment)
                if (installment.payerCosts.isEmpty()) {
                    showEmpty()
                }
            } catch (e: RequestException) {
               showError()
            }
            hideLoading()
        }
    }

    private fun showLoading() {
        loadingState.postValue(State.Layout.Loading(true))
    }

    private fun hideLoading() {
        loadingState.postValue(State.Layout.Loading(false))
    }

    private fun enablePayButton() {
        payButtonState.postValue(State.Layout.PayButton(true))
    }

    private fun disablePayButton() {
        payButtonState.postValue(State.Layout.PayButton(false))
    }

    private fun showEmpty() {
        listState.postValue(State.Received.Empty)
    }

    private fun showError() {
        listState.postValue(State.Received.Error)
    }

    sealed class State {
        sealed class Layout : State() {
            data class Loading(val loading: Boolean) : Layout()
            data class PayButton(val enable: Boolean) : Layout()
            data class AmountText(val text: String) : Layout()
        }

        sealed class Received : State() {
            object Empty : Received()
            object Error : Received()
        }
    }
}