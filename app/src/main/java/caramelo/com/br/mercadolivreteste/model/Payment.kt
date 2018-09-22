package caramelo.com.br.mercadolivreteste.model

import android.app.Activity
import android.os.Parcelable
import dagger.Module
import dagger.Provides
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment(
    var value: Float = 0f,
    var paymentMethod: PaymentMethod? = null
) : Parcelable


const val EXTRA_PAYMENT = "extra_payment"

@Module
class PaymentModule {

    @Provides
    fun providePayment(activity: Activity) : Payment {
        return activity.intent.getParcelableExtra(EXTRA_PAYMENT)
    }
}