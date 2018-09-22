package caramelo.com.br.mercadolivreteste.repository

import caramelo.com.br.mercadolivreteste.BuildConfig
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.extension.request
import caramelo.com.br.mercadolivreteste.model.Bank
import caramelo.com.br.mercadolivreteste.model.PaymentMethod
import javax.inject.Inject

class PaymentRepository @Inject constructor(
        private val api: PaymentApi
) {

    private val publicKey by lazy { BuildConfig.PUBLIC_KEY }

    @Throws(RequestException::class)
    suspend fun creditCardPaymentMethods(): List<PaymentMethod> {
        val list = api.paymentMethods(publicKey).request()
        return list.filter { it.type == "credit_card" && it.status == "active" }
    }

    @Throws(RequestException::class)
    suspend fun banks(paymentMethodId: String): List<Bank> {
        return api.banks(publicKey, paymentMethodId).request()
    }

}