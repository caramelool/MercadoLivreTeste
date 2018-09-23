package caramelo.com.br.mercadolivreteste.ui.amount

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.CurrentMaskListener
import caramelo.com.br.mercadolivreteste.extension.addCurrentMask
import caramelo.com.br.mercadolivreteste.extension.bind
import caramelo.com.br.mercadolivreteste.ui.amount.AmountViewModel.State
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodActivity
import dagger.android.AndroidInjection

import javax.inject.Inject

class AmountActivity : BaseActivity(), CurrentMaskListener {

    private val edtAmount: EditText by bind(R.id.activity_amount_edit_amount)
    private val btnNext: Button by bind(R.id.activity_amount_btn_next)

    @Inject
    lateinit var viewModel: AmountViewModel

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AmountActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_amount)
        setSupportActionBar(toolbar)

        edtAmount.addCurrentMask(this)

        btnNext.setOnClickListener {
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
        viewModel.setAmount(value)
    }

    private fun handlerLayout(state: State.Layout) {
        when(state) {
            is State.Layout.NextButton -> {
                btnNext.isEnabled = state.enable
            }
        }
    }

    private fun goToPaymentMethod() {
        val intent = PaymentMethodActivity.getIntent(
                this, viewModel.payment)
        startActivity(intent)
    }
}
