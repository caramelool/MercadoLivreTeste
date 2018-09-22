package caramelo.com.br.mercadolivreteste

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import caramelo.com.br.mercadolivreteste.dagger.DaggerApplicationComponent
import caramelo.com.br.mercadolivreteste.dagger.module.ApplicationModule

import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import java.util.*
import javax.inject.Inject

class MercadoLivreApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        updateLocale()
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
                .inject(this)
    }

    override fun activityInjector() = dispatchingActivityInjector

    private fun updateLocale() {
        val locale = Locale.US
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        createConfigurationContext(config)
    }
}