package caramelo.com.br.mercadolivreteste.ui.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.extra
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_payment_method.*
import javax.inject.Inject

class PaymentMethodActivity : BaseActivity(), PaymentMethodView {

    @Inject
    lateinit var presenter: PaymentMethodPresenter

    private val payment: Payment by extra(EXTRA_PAYMENT)

    companion object {

        private const val EXTRA_PAYMENT = "extra_payment"

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

        presenter.payment = payment

        lifecycle.addObserver(presenter)
    }

}
