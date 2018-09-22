package caramelo.com.br.mercadolivreteste.ui.bank

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.VisibleForTesting
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
    @VisibleForTesting val buttonState = MutableLiveData<State>()
    @VisibleForTesting val listState = MutableLiveData<State>()

    val state: MediatorLiveData<State>
        get() = MediatorLiveData<State>().apply {
            addSource(loadingState)
            addSource(buttonState)
            addSource(listState)
        }

    private var bankList = listOf<Bank>()

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
        runJob {
            showLoading()
            try {
                bankList = repository.banks(payment.methodId)
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
    }

    fun setBank(id: String) {
        payment.bankId = id
        enableNextButton()
    }

    private fun showLoading() {
        loadingState.postValue(State.Layout.Loading(true))
    }

    private fun hideLoading() {
        loadingState.postValue(State.Layout.Loading(false))
    }

    private fun enableNextButton() {
        buttonState.postValue(State.Layout.NextButton(true))
    }

    private fun disableNextButton() {
        buttonState.postValue(State.Layout.NextButton(false))
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

    sealed class Layout : State() {
        data class Loading(val loading: Boolean) : Layout()
        data class NextButton(val enable: Boolean) : Layout()
    }

    sealed class Received : State() {
        data class Banks(val list: List<Bank>) : Received()
        class Empty : Received()
        class Error : Received()
    }
}