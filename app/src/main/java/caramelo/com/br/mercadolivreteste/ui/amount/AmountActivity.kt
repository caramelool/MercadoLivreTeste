package caramelo.com.br.mercadolivreteste.ui.amount

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.CurrentMaskListener
import caramelo.com.br.mercadolivreteste.extension.addCurrentMask
import caramelo.com.br.mercadolivreteste.ui.amount.AmountState as State
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodActivity
import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_amount.*
import kotlinx.android.synthetic.main.content_amount.*
import javax.inject.Inject

class AmountActivity : BaseActivity(), CurrentMaskListener {

    @Inject
    lateinit var viewModel: AmountViewModel

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AmountActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_amount)
        setSupportActionBar(toolbar)

        amountEditText.addCurrentMask(this)

        nextButton.setOnClickListener {
            goToPaymentMethod()
        }

        viewModel.state.observe(this, Observer { state ->
            when(state) {
                is State.Layout -> handlerLayout(state)
            }
        })

        lifecycle.addObserver(viewModel)
    }

    override fun onValueChange(value: Float) {
        viewModel.amount = value
    }

    private fun handlerLayout(state: State.Layout) {
        when(state) {
            is State.Layout.NextButton -> {
                nextButton.isEnabled = state.enable
            }
        }
    }

    private fun goToPaymentMethod() {
        val intent = PaymentMethodActivity.getIntent(
                this, viewModel.payment)
        startActivity(intent)
    }
}
