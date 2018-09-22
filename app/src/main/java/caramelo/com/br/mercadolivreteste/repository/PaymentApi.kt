package caramelo.com.br.mercadolivreteste.repository

import caramelo.com.br.mercadolivreteste.model.Bank
import caramelo.com.br.mercadolivreteste.model.PaymentMethod
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface PaymentApi {
    @GET("v1/payment_methods")
    fun paymentMethods(@Query("public_key") publicKey: String): Deferred<List<PaymentMethod>>

    @GET("v1/payment_methods/card_issuers")
    fun banks(@Query("public_key") publicKey: String,
              @Query("payment_method_id") paymentMethodId: String): Deferred<List<Bank>>
}