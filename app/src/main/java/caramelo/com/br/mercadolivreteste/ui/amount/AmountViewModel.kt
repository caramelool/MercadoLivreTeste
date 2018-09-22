package caramelo.com.br.mercadolivreteste.ui.amount

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import caramelo.com.br.mercadolivreteste.extension.addSource
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel

class AmountViewModel : BaseViewModel() {

    private val buttonState = MutableLiveData<State>()

    val state = MediatorLiveData<State>().apply {
        addSource(buttonState)
    }

    val payment by lazy { Payment() }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun initState() {
        if (buttonState.value == null) {
            disableNextButton()
        }
    }

    fun setAmount(amount: Float) {
        payment.amount = amount
        if (amount > 0f) {
            enableNextButton()
        } else {
            disableNextButton()
        }
    }

    private fun enableNextButton() {
        buttonState.postValue(State.Layout.NextButton(true))
    }

    private fun disableNextButton() {
        buttonState.postValue(State.Layout.NextButton(false))
    }

    sealed class State {
        sealed class Layout : State() {
            data class NextButton(val enable: Boolean) : Layout()
        }
    }
}