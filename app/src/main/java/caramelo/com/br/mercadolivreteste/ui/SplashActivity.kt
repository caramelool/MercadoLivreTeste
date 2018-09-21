package caramelo.com.br.mercadolivreteste.ui

import android.os.Bundle
import caramelo.com.br.mercadolivreteste.ui.base.BaseActivity
import caramelo.com.br.mercadolivreteste.ui.value.ValueActivity
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch(UI) {
            delay(1, TimeUnit.SECONDS)
            openValue()
        }
    }

    private fun openValue() {
        val intent = ValueActivity.getIntent(this)
        startActivity(intent)
        finish()
    }
}