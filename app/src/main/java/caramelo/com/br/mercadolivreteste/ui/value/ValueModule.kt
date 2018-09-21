package caramelo.com.br.mercadolivreteste.ui.value

import dagger.Binds
import dagger.Module

@Module
abstract class ValueModule {
    @Binds
    abstract fun bindView(activity: ValueActivity): ValueView
}