package caramelo.com.br.mercadolivreteste.ui.installment

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.bind
import caramelo.com.br.mercadolivreteste.model.EXTRA_PAYMENT
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.amount.AmountActivity
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.installment.InstallmentViewModel.State
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_empty.view.*
import kotlinx.android.synthetic.main.view_success.*
import kotlinx.android.synthetic.main.view_success.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InstallmentActivity : BaseActivity() {

    private val loading: ProgressBar by bind(R.id.activity_installment_loading)
    private val recycler: RecyclerView by bind(R.id.activity_installment_recycler)
    private val txtInstallment: TextView by bind(R.id.activity_installment_txt_amount)
    private val btnPay: Button by bind(R.id.activity_installment_btn_pay)

    @Inject
    lateinit var viewModel: InstallmentViewModel

    private val adapter by lazy { InstallmentAdapter(viewModel.payment, ::onInstallmentsSelected) }

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

        recycler.apply {
            layoutManager = LinearLayoutManager(this@InstallmentActivity)
            setHasFixedSize(true)
            adapter = this@InstallmentActivity.adapter
        }

        btnPay.setOnClickListener {
            viewModel.pay()
        }

        viewModel.state.observe(this, Observer { state ->
            when(state) {
                is State.Layout -> handlerLayout(state)
                is State.Received -> handlerReceived(state)
            }
        })

        lifecycle.addObserver(viewModel)
    }

    private fun handlerLayout(state: State.Layout) {
        when(state) {
            is State.Layout.Loading -> {
                loading.visibility = if (state.loading) {
                    with(View.GONE) {
                        btnPay.visibility = this
                        recycler.visibility = this
                        txtInstallment.visibility = this
                    }
                    View.VISIBLE
                } else {
                    with(View.VISIBLE) {
                        btnPay.visibility = this
                        recycler.visibility = this
                        txtInstallment.visibility = this
                    }
                    View.GONE
                }
            }
            is State.Layout.PayButton -> {
                btnPay.isEnabled = state.enable
            }
            is State.Layout.InstallmentText -> {
                txtInstallment.text = state.text
            }
        }

    }

    private fun handlerReceived(state: State.Received) {
        when(state) {
            is State.Received.Info -> {
                adapter.setInstallment(state.installment)
            }
            is State.Received.Empty,
            is State.Received.Error -> {
                btnPay.visibility = View.GONE
                txtInstallment.visibility = View.GONE
                with(emptyView) {
                    visibility = View.VISIBLE
                    emptyText.setText(R.string.installment_error_message)
                }
            }
            is State.Received.Paid -> showPaidSuccess()
        }
    }

    private fun showPaidSuccess() {
        launch(UI) {
            loading.visibility = View.GONE
            with(successView) {
                visibility = View.VISIBLE
                successLottie.playAnimation()
                successText.setText(R.string.payment_paid_successfully_message)
            }
            delay(3, TimeUnit.SECONDS)
            goToAmount()
        }
    }

    private fun onInstallmentsSelected(installments: Int) {
        viewModel.setInstallments(installments)
    }

    private fun goToAmount() {
        val intent = AmountActivity.getIntent(this@InstallmentActivity)
        startActivity(intent)
    }

}
