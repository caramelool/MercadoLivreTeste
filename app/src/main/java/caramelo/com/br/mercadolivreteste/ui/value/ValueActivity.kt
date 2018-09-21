package caramelo.com.br.mercadolivreteste.ui.value

import android.content.Context
import android.content.Intent
import android.os.Bundle
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.CurrentMaskListener
import caramelo.com.br.mercadolivreteste.extension.addCurrentMask
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodActivity
import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_value.*
import kotlinx.android.synthetic.main.content_value.*
import javax.inject.Inject

class ValueActivity : BaseActivity(), ValueView, CurrentMaskListener {

    @Inject
    lateinit var presenter: ValuePresenter

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, ValueActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_value)
        setSupportActionBar(toolbar)

        valueEditText.addCurrentMask(this)

        nextButton.setOnClickListener {
            openPaymentMethod()
        }
    }

    override fun onValueChange(value: Float) {
        presenter.value = value
    }

    override fun disableButton() {
        nextButton.isEnabled = false
    }

    override fun enableButton() {
        nextButton.isEnabled = true
    }

    private fun openPaymentMethod() {
        val intent = PaymentMethodActivity.getIntent(
                this, presenter.payment)
        startActivity(intent)
    }
}
