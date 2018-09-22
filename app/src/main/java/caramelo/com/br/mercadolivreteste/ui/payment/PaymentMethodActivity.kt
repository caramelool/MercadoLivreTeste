package caramelo.com.br.mercadolivreteste.ui.payment

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.model.EXTRA_PAYMENT
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.base.ItemAdapter
import caramelo.com.br.mercadolivreteste.ui.base.ItemData
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_payment_method.*
import kotlinx.android.synthetic.main.content_payment_method.*
import javax.inject.Inject

class PaymentMethodActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: PaymentMethodViewModel

    private val adapter by lazy { ItemAdapter(::onItemSelected) }

    companion object {
        fun getIntent(context: Context, payment: Payment): Intent {
            return Intent(context, PaymentMethodActivity::class.java)
                    .putExtra(EXTRA_PAYMENT, payment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_payment_method)
        setSupportActionBar(toolbar, true)

        paymentMethodRecyclerView.adapter = adapter
        paymentMethodRecyclerView.setHasFixedSize(true)

//        viewModel.loadingState.observe(this, Observer { state ->
//            when(state) {
//                is PaymentMethodState.Loading -> handlerLoading(state)
//            }
//        })
//
//        viewModel.buttonState.observe(this, Observer { state ->
//            when(state) {
//                is PaymentMethodState.Changes -> handlerChanges(state)
//            }
//        })
//
//        viewModel.listState.observe(this, Observer { state ->
//            when(state) {
//                is PaymentMethodState.Received -> handlerReceived(state)
//                is PaymentMethodState.Error -> handlerError()
//            }
//        })

        viewModel.state.observe(this, Observer { state ->
            when(state) {
                is PaymentMethodState.Loading -> handlerLoading(state)
                is PaymentMethodState.Changes -> handlerChanges(state)
                is PaymentMethodState.Received -> handlerReceived(state)
                is PaymentMethodState.Error -> handlerError()
            }
        })
    }

    private fun handlerChanges(state: PaymentMethodState.Changes) {
        when(state) {
            is PaymentMethodState.Changes.NextButton -> {
                nextButton.isEnabled = state.enable
            }
        }
    }

    private fun handlerLoading(state: PaymentMethodState.Loading) {
        loading.visibility = if (state.loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun handlerReceived(state: PaymentMethodState.Received) {
        when(state) {
            is PaymentMethodState.Received.PaymentMethods -> {
                val data = state.list.map {
                    ItemData(it.id, it.name, it.thumbnail,
                        it == viewModel.payment.paymentMethod)
                }
                adapter.data = data
            }
        }
    }

    private fun handlerError() {

    }

    private fun onItemSelected(id: String) {
        viewModel.setPaymentMethod(id)
    }
}
