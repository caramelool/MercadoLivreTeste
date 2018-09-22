package caramelo.com.br.mercadolivreteste.repository

import caramelo.com.br.mercadolivreteste.BuildConfig
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.extension.request
import caramelo.com.br.mercadolivreteste.model.PaymentMethod
import javax.inject.Inject

class PaymentRepository @Inject constructor(
        private val api: PaymentApi
) {

    @Throws(RequestException::class)
    suspend fun creditCardPaymentMethods(): List<PaymentMethod> {
        val list = api.paymentMethods(BuildConfig.PUBLIC_KEY).request()
        return list.filter { it.type == "credit_card" && it.status == "active" }
    }

}