package caramelo.com.br.mercadolivreteste.ui.installment

import android.arch.lifecycle.*
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.extension.addSource
import caramelo.com.br.mercadolivreteste.model.Installment
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel
import kotlinx.coroutines.experimental.delay

class InstallmentViewModel(
        val payment: Payment,
        val repository: PaymentRepository
) : BaseViewModel() {

    private val loadingState = MutableLiveData<State>()
    private val payButtonState = MutableLiveData<State>()
    private val amountTextState = MutableLiveData<State>()
    private val installmentState = MutableLiveData<State>()

    val state: MediatorLiveData<State>
        get() = MediatorLiveData<State>().apply {
            addSource(loadingState)
            addSource(payButtonState)
            addSource(amountTextState)
            addSource(installmentState)
        }

    lateinit var installment: Installment

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        if (payButtonState.value == null) {
            disablePayButton()
        }

        if (installmentState.value == null) {
            requestInstallment()
        }
    }

    fun requestInstallment() {
        runJob {
            showLoading()
            try {
                installment = repository.installment(payment)
                showInfo()
                setRecommendedInstallments()
            } catch (e: RequestException) {
               showError()
            }
            hideLoading()
        }
    }

    fun setRecommendedInstallments() {
        installment.payerCosts
                .find { it.isRecommended() }
                ?.let { item ->
                    payment.instalmments = item.installments
                    setInstallmentText(item.recommendedMessage)
                    enablePayButton()
                }
    }

    fun setInstallments(installments: Int) {
        installment.payerCosts
                .find { it.installments == installments }
                ?.let { item ->
                    payment.instalmments = item.installments
                    setInstallmentText(item.recommendedMessage)
                    enablePayButton()
                }

    }

    fun pay() {
        runJob {
            showLoading()
            delay(3000)
            showPaid()
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

    private fun showInfo() {
        installmentState.postValue(State.Received.Info(installment))
    }

    private fun showEmpty() {
        installmentState.postValue(State.Received.Empty)
    }

    private fun showError() {
        installmentState.postValue(State.Received.Error)
    }

    private fun setInstallmentText(message: String) {
        amountTextState.postValue(State.Layout.InstallmentText(message))
    }

    private fun showPaid() {
        installmentState.postValue(State.Received.Paid)
    }

    sealed class State {
        sealed class Layout : State() {
            data class Loading(val loading: Boolean) : Layout()
            data class PayButton(val enable: Boolean) : Layout()
            data class InstallmentText(val text: String) : Layout()
        }

        sealed class Received : State() {
            data class Info(val installment: Installment): Received()
            object Paid : Received()
            object Empty : Received()
            object Error : Received()
        }
    }
}