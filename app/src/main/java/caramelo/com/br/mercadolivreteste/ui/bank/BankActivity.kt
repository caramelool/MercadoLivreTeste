package caramelo.com.br.mercadolivreteste.ui.bank

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.bind
import caramelo.com.br.mercadolivreteste.model.EXTRA_PAYMENT
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.bank.BankViewModel.State
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.base.ItemAdapter
import caramelo.com.br.mercadolivreteste.ui.base.ItemData
import caramelo.com.br.mercadolivreteste.ui.installment.InstallmentActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_empty.view.*
import javax.inject.Inject

class BankActivity : BaseActivity() {

    private val loading: ProgressBar by bind(R.id.activity_bank_loading)
    private val recycler: RecyclerView by bind(R.id.activity_bank_recycler)
    private val btnNext: Button by bind(R.id.activity_bank_btn_next)

    @Inject
    lateinit var viewModel: BankViewModel

    private val adapter by lazy { ItemAdapter(R.drawable.ic_bank_placeholder, ::onItemSelected) }

    companion object {
        fun getIntent(context: Context, payment: Payment): Intent {
            return Intent(context, BankActivity::class.java)
                    .putExtra(EXTRA_PAYMENT, payment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_bank)
        setSupportActionBar(toolbar, true)

        recycler.adapter = adapter
        recycler.setHasFixedSize(true)

        btnNext.setOnClickListener {
            goToInstallment()
        }

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is State.Layout -> handlerLayout(state)
                is State.Received -> handlerReceived(state)
            }
        })

        lifecycle.addObserver(viewModel)
    }

    private fun handlerLayout(state: State.Layout) {
        when (state) {
            is State.Layout.Loading -> {
                loading.visibility = if (state.loading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            is State.Layout.NextButton -> {
                btnNext.isEnabled = state.enable
            }
        }

    }

    private fun handlerReceived(state: State.Received) {
        when (state) {
            is State.Received.Banks -> {
                val data = state.list.map {
                    ItemData(it.id, it.name, it.thumbnail,
                            it.id == viewModel.payment.bankId)
                }
                adapter.data = data
            }
            is State.Received.Empty,
            is State.Received.Error -> handlerError()
        }
    }

    private fun handlerError() {
        btnNext.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.emptyText.setText(R.string.bank_list_empty_message)
    }

    private fun onItemSelected(id: String) {
        viewModel.setBank(id)
    }

    private fun goToInstallment() {
        val intent = InstallmentActivity.getIntent(this, viewModel.payment)
        startActivity(intent)
    }
}
