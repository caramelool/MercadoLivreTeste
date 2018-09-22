package caramelo.com.br.mercadolivreteste.ui.value

import android.arch.lifecycle.Observer
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

class ValueActivity : BaseActivity(), CurrentMaskListener {

    @Inject
    lateinit var viewModel: ValueViewModel

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

        viewModel.state.observe(this, Observer { state ->
            when(state) {
                is ValueState.Changes -> handlerChanges(state)
            }
        })
    }

    override fun onValueChange(value: Float) {
        viewModel.value = value
    }

    private fun handlerChanges(state: ValueState.Changes) {
        when(state) {
            is ValueState.Changes.NextButton -> {
                nextButton.isEnabled = state.enable
            }
        }
    }

    private fun openPaymentMethod() {
        val intent = PaymentMethodActivity.getIntent(
                this, viewModel.payment)
        startActivity(intent)
    }
}
