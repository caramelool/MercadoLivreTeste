package caramelo.com.br.mercadolivreteste.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Installment(
        @SerializedName("payment_method_id") val paymentMethodId: String,
        @SerializedName("payment_type_id") val paymentMethodType: String,
        @SerializedName("issuer") val issuer: InstallmentIssuer,
        @SerializedName("payer_costs") val payerCosts: List<InstallmentItem>
) : Parcelable

@Parcelize
data class InstallmentIssuer(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("thumbnail") val thumbnail: String
) : Parcelable

@Parcelize
data class InstallmentItem(
        @SerializedName("labels") val labels: List<String>,
        @SerializedName("recommended_message") val recommendedMessage: String,
        @SerializedName("installments") val installments: Int,
        @SerializedName("installment_rate") val installmentRate: Float,
        @SerializedName("installment_amount") val installmentAmount: Float,
        @SerializedName("total_amount") val totalAmount: Float
) : Parcelable {

    fun isRecommended() = labels.contains("recommended_installment")
}