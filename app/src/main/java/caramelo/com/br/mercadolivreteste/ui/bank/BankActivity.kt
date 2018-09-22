package caramelo.com.br.mercadolivreteste.ui.bank

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.model.EXTRA_PAYMENT
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.bank.BankState as State
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.base.ItemAdapter
import caramelo.com.br.mercadolivreteste.ui.base.ItemData
import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_bank.*
import kotlinx.android.synthetic.main.content_bank.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_empty.view.*
import javax.inject.Inject

class BankActivity : BaseActivity() {

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

        bankRecyclerView.adapter = adapter
        bankRecyclerView.setHasFixedSize(true)

        viewModel.state.observe(this, Observer { state ->
            when(state) {
                is State.Loading -> handlerLoading(state)
                is State.Changes -> handlerChanges(state)
                is State.Received -> handlerReceived(state)
            }
        })

        lifecycle.addObserver(viewModel)
    }

    private fun handlerChanges(state: State.Changes) {
        when(state) {
            is State.Changes.NextButton -> {
                nextButton.isEnabled = state.enable
            }
        }
    }

    private fun handlerLoading(state: State.Loading) {
        loading.visibility = if (state.loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun handlerReceived(state: State.Received) {
        when(state) {
            is State.Received.Banks -> {
                val data = state.list.map {
                    ItemData(it.id, it.name, it.thumbnail,
                            it == viewModel.payment.bank)
                }
                adapter.data = data
            }
            is State.Received.Empty,
            is State.Received.Error -> handlerError()
        }
    }

    private fun handlerError() {
        nextButton.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.emptyText.setText(R.string.bank_list_empty_message)
    }

    private fun onItemSelected(id: String) {
        viewModel.setBank(id)
    }

    private fun goToBank() {
        val intent = BankActivity.getIntent(this, viewModel.payment)
        startActivity(intent)
    }
}
