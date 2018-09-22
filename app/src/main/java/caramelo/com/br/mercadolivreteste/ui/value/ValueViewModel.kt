package caramelo.com.br.mercadolivreteste.ui.value

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting
import caramelo.com.br.mercadolivreteste.model.Payment

class ValueViewModel : ViewModel() {

    val state = MutableLiveData<ValueState>()
        get() {
            if (field.value == null) {
                field.value = ValueState.Idle()
                disableNextButton()
            }
            return field
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

    private fun enableNextButton() {
        state.postValue(ValueState.Changes.NextButton(true))
    }

    private fun disableNextButton() {
        state.postValue(ValueState.Changes.NextButton(false))
    }
}

sealed class ValueState {
    class Idle : ValueState()
    sealed class Changes : ValueState() {
        data class NextButton(val enable: Boolean) : Changes()
    }
}