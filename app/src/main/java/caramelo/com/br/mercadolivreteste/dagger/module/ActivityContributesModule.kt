package caramelo.com.br.mercadolivreteste.dagger.module

import caramelo.com.br.mercadolivreteste.model.PaymentModule
import caramelo.com.br.mercadolivreteste.ui.bank.BankActivity
import caramelo.com.br.mercadolivreteste.ui.bank.BankModule
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodActivity
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodModule
import caramelo.com.br.mercadolivreteste.ui.value.ValueActivity
import caramelo.com.br.mercadolivreteste.ui.value.ValueModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityContributesModule {
    @ContributesAndroidInjector(modules = [ValueModule::class])
    abstract fun contributesValueActivity() : ValueActivity

    @ContributesAndroidInjector(modules = [PaymentMethodModule::class, PaymentModule::class])
    abstract fun contributesPaymentMethodActivity() : PaymentMethodActivity

    @ContributesAndroidInjector(modules = [BankModule::class, PaymentModule::class])
    abstract fun contributesBankActivity() : BankActivity
}