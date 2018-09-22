package caramelo.com.br.mercadolivreteste.ui.payment

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import dagger.Module
import dagger.Provides

@Module
class PaymentMethodModule {

    @Provides
    fun provideActivity(activity: PaymentMethodActivity): Activity {
        return activity
    }

    @Provides
    fun provideViewHolder(
            activity: PaymentMethodActivity,
            payment: Payment,
            repository: PaymentRepository
    ): PaymentMethodViewModel {
        return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PaymentMethodViewModel(payment, repository) as T
            }
        })[PaymentMethodViewModel::class.java]
    }
}