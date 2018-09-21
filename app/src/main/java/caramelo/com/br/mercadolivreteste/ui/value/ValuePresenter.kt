package caramelo.com.br.mercadolivreteste.ui.value

import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.base.BasePresenter
import javax.inject.Inject

class ValuePresenter @Inject constructor(
        private val view: ValueView
) : BasePresenter() {

    var value: Float = 0f
        set(value) {
            field = value
            if (field > 0f) {
                view.enableButton()
            } else {
                view.disableButton()
            }
        }

    val payment: Payment
        get() = Payment(value)
}

interface ValueView {
    fun disableButton()
    fun enableButton()
}