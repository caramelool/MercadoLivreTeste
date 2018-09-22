package caramelo.com.br.mercadolivreteste.ui

import caramelo.com.br.mercadolivreteste.R
import android.os.Bundle
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.amount.AmountActivity
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        launch(UI) {
            delay(1, TimeUnit.SECONDS)
            openAmount()
        }
    }

    private fun openAmount() {
        val intent = AmountActivity.getIntent(this)
        startActivity(intent)
        finish()
    }
}