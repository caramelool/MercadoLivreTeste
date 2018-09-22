package caramelo.com.br.mercadolivreteste.ui.bank

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.extension.addSource
import caramelo.com.br.mercadolivreteste.model.Bank
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel
import caramelo.com.br.mercadolivreteste.ui.bank.BankState as State

class BankViewModel(
        val payment: Payment,
        private val repository: PaymentRepository
) : BaseViewModel() {

    private val loadingState = MutableLiveData<State>()
    private val buttonState = MutableLiveData<State>()
    private val listState = MutableLiveData<State>()

    val state: MediatorLiveData<State>
        get() = MediatorLiveData<State>().apply {
            addSource(loadingState)
            addSource(buttonState)
            addSource(listState)
        }

    val bankList = mutableListOf<Bank>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        if (listState.value == null) {
            requestBanks()
        }
        if (buttonState.value == null) {
            disableNextButton()
        }
    }

    fun requestBanks() {
        payment.paymentMethod?.id?.let { paymentMethodId ->
            runJob {
                showLoading()
                try {
                    bankList.apply {
                        clear()
                        addAll(repository.banks(paymentMethodId))
                    }
                    if (bankList.isNotEmpty()) {
                        showPaymentMethods()
                    } else {
                        showEmpty()
                    }
                } catch (e: RequestException) {
                    showError()
                }
                hideLoading()
            }
        } ?: throw IllegalStateException()
    }

    fun setBank(id: String) {
        val bank = bankList.find { it.id == id }
        payment.bank = bank
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

    private fun showEmpty() {
        listState.postValue(State.Received.Empty())
    }

    private fun showPaymentMethods() {
        listState.postValue(State.Received.Banks(bankList))
    }

    private fun showError() {
        listState.postValue(State.Received.Error())
    }

}

sealed class BankState {
    data class Loading(val loading: Boolean) : State()

    sealed class Changes : State() {
        data class NextButton(val enable: Boolean) : Changes()
    }



    sealed class Received : State() {
        data class Banks(val list: List<Bank>) : Received()
        class Empty : Received()
        class Error : Received()
    }
}