package caramelo.com.br.mercadolivreteste.ui.value

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import caramelo.com.br.mercadolivreteste.extension.addSource
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel

class ValueViewModel : BaseViewModel() {

    private val buttonState = MutableLiveData<ValueState>()

    val state = MediatorLiveData<ValueState>().apply {
        addSource(buttonState)
    }

    var value: Float = 0f
        set(value) {
            field = value
            if (field > 0f) {
                enableNextButton()
            } else {
                disableNextButton()
            }
        }

    val payment: Payment
        get() = Payment(value)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun initState() {
        if (buttonState.value == null) {
            disableNextButton()
        }
    }

    private fun enableNextButton() {
        buttonState.postValue(ValueState.Changes.NextButton(true))
    }

    private fun disableNextButton() {
        buttonState.postValue(ValueState.Changes.NextButton(false))
    }
}

sealed class ValueState {
    sealed class Changes : ValueState() {
        data class NextButton(val enable: Boolean) : Changes()
    }
}