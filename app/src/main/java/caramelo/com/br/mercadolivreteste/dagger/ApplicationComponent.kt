package caramelo.com.br.mercadolivreteste.dagger

import caramelo.com.br.mercadolivreteste.MercadoLivreApplication
import caramelo.com.br.mercadolivreteste.dagger.module.ActivityContributesModule
import caramelo.com.br.mercadolivreteste.dagger.module.ApiModule
import caramelo.com.br.mercadolivreteste.dagger.module.ApplicationModule
import caramelo.com.br.mercadolivreteste.dagger.module.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    RetrofitModule::class,
    ApiModule::class,
    ActivityContributesModule::class
])
interface ApplicationComponent {
    fun inject(application: MercadoLivreApplication)
}