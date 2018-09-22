package caramelo.com.br.mercadolivreteste.repository

import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.extension.request
import caramelo.com.br.mercadolivreteste.model.Bank
import caramelo.com.br.mercadolivreteste.model.Installment
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.model.PaymentMethod
import javax.inject.Inject

class PaymentRepository @Inject constructor(
        private val api: PaymentApi
) {

    @Throws(RequestException::class)
    suspend fun creditCardPaymentMethods(): List<PaymentMethod> {
        val list = api.paymentMethods().request()
        return list.filter { it.type == "credit_card" && it.status == "active" }
    }

    @Throws(RequestException::class)
    suspend fun banks(paymentMethodId: String): List<Bank> {
        return api.banks(paymentMethodId).request()
    }

    @Throws(RequestException::class)
    suspend fun installment(payment: Payment): Installment {
        return api.installments(
                payment.amount,
                payment.methodId,
                payment.bankId).request()
    }

}