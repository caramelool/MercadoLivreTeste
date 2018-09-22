package caramelo.com.br.mercadolivreteste.ui.installment

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.model.EXTRA_PAYMENT
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.installment.InstallmentViewModel.State
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_installment.*
import kotlinx.android.synthetic.main.content_installment.*
import javax.inject.Inject

class InstallmentActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: InstallmentViewModel

    companion object {
        fun getIntent(context: Context, payment: Payment): Intent {
            return Intent(context, InstallmentActivity::class.java)
                    .putExtra(EXTRA_PAYMENT, payment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_installment)
        setSupportActionBar(toolbar, true)

        viewModel.state.observe(this, Observer { state ->
            when(state) {
                is State.Layout -> handlerLayout(state)
                is State.Received -> handlerReceived(state)
            }
        })
    }

    private fun handlerLayout(state: State.Layout) {
        when(state) {
            is State.Layout.Loading -> {
                loading.visibility = if (state.loading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            is State.Layout.PayButton -> {
                payButton.isEnabled = state.enable
            }
        }

    }

    private fun handlerReceived(state: State.Received) {

    }

}
