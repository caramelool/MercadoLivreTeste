package caramelo.com.br.mercadolivreteste.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentMethod(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("payment_type_id") val type: String,
        @SerializedName("status") val status: String,
        @SerializedName("thumbnail") val thumbnail: String
) : Parcelable