package caramelo.com.br.mercadolivreteste.ui.installment

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel.*
import dagger.Module
import dagger.Provides

@Module
class InstallmentModule {
    @Provides
    fun provideActivity(activity: InstallmentActivity) : Activity {
        return activity
    }

    @Provides
    fun provideViewModel(
            activity: InstallmentActivity,
            payment: Payment,
            repository: PaymentRepository
    ) : InstallmentViewModel {
        return ViewModelProviders.of(activity, Factory {
            InstallmentViewModel(payment, repository)
        })[InstallmentViewModel::class.java]
    }
}