package caramelo.com.br.mercadolivreteste.ui.payment

import dagger.Binds
import dagger.Module

@Module
abstract class PaymentMethodModule {
    @Binds
    abstract fun bindView(activity: PaymentMethodActivity): PaymentMethodView
}