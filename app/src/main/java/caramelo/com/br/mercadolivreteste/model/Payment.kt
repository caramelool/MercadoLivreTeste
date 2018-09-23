package caramelo.com.br.mercadolivreteste.model

import android.app.Activity
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.Provides
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment(
        var amount: Float = 0f,
        var methodId: String = "",
        var bankId: String = "",
        var instalmments: Int = 0
) : Parcelable

@Parcelize
data class PaymentMethod(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("payment_type_id") val type: String,
        @SerializedName("status") val status: String,
        @SerializedName("thumbnail") val thumbnail: String
) : Parcelable

const val EXTRA_PAYMENT = "extra_payment"

@Module
class PaymentModule {

    @Provides
    fun providePayment(activity: Activity) : Payment {
        return activity.intent.getParcelableExtra(EXTRA_PAYMENT)
    }
}