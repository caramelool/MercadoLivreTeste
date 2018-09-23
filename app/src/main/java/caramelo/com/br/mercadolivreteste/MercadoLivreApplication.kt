package caramelo.com.br.mercadolivreteste

import android.app.Activity
import android.app.Application
import caramelo.com.br.mercadolivreteste.dagger.DaggerApplicationComponent
import caramelo.com.br.mercadolivreteste.dagger.module.ApplicationModule

import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MercadoLivreApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
                .inject(this)
    }

    override fun activityInjector() = dispatchingActivityInjector
}