package caramelo.com.br.mercadolivreteste.ui.payment

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.base.BaseViewModel.Factory
import dagger.Module
import dagger.Provides

@Module
class PaymentMethodModule {

    @Provides
    fun provideActivity(activity: PaymentMethodActivity): Activity {
        return activity
    }

    @Provides
    fun provideViewModel(
            activity: PaymentMethodActivity,
            payment: Payment,
            repository: PaymentRepository
    ): PaymentMethodViewModel {
        return ViewModelProviders.of(activity, Factory {
            PaymentMethodViewModel(payment, repository)
        })[PaymentMethodViewModel::class.java]
    }
}