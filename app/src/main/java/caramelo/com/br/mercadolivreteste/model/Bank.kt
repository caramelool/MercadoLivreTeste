package caramelo.com.br.mercadolivreteste.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bank(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("processing_mode") val processingMode: String
) : Parcelable