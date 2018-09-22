package caramelo.com.br.mercadolivreteste.ui.bank

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel.Factory
import dagger.Module
import dagger.Provides

@Module
class BankModule {

    @Provides
    fun provideActivity(bankActivity: BankActivity): Activity {
        return bankActivity
    }

    @Provides
    fun provideViewHolder(
            activity: BankActivity,
            payment: Payment,
            repository: PaymentRepository
    ): BankViewModel {
        return ViewModelProviders.of(activity, Factory {
            BankViewModel(payment, repository)
        })[BankViewModel::class.java]
    }

}