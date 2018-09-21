package caramelo.com.br.mercadolivreteste.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment(
    var value: Float = 0f,
    var paymentMethod: PaymentMethod? = null
) : Parcelable