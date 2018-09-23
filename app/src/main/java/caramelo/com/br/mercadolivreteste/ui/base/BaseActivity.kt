package caramelo.com.br.mercadolivreteste.ui.base

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import caramelo.com.br.mercadolivreteste.R
import caramelo.com.br.mercadolivreteste.extension.bind

abstract class BaseActivity : AppCompatActivity() {

    protected val toolbar: Toolbar by bind(R.id.toolbar)

    fun setSupportActionBar(toolbar: Toolbar, showHome: Boolean) {
        setSupportActionBar(toolbar)
        if (showHome) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}