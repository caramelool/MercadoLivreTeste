package caramelo.com.br.mercadolivreteste.dagger.module

import caramelo.com.br.mercadolivreteste.model.PaymentModule
import caramelo.com.br.mercadolivreteste.ui.amount.AmountActivity
import caramelo.com.br.mercadolivreteste.ui.amount.AmountModule
import caramelo.com.br.mercadolivreteste.ui.bank.BankActivity
import caramelo.com.br.mercadolivreteste.ui.bank.BankModule
import caramelo.com.br.mercadolivreteste.ui.installment.InstallmentActivity
import caramelo.com.br.mercadolivreteste.ui.installment.InstallmentModule
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodActivity
import caramelo.com.br.mercadolivreteste.ui.payment.PaymentMethodModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityContributesModule {
    @ContributesAndroidInjector(modules = [AmountModule::class])
    abstract fun contributesValueActivity() : AmountActivity

    @ContributesAndroidInjector(modules = [PaymentMethodModule::class, PaymentModule::class])
    abstract fun contributesPaymentMethodActivity() : PaymentMethodActivity

    @ContributesAndroidInjector(modules = [BankModule::class, PaymentModule::class])
    abstract fun contributesBankActivity() : BankActivity

    @ContributesAndroidInjector(modules = [InstallmentModule::class, PaymentModule::class])
    abstract fun contributesInstallmentActivity() : InstallmentActivity
}