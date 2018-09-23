package caramelo.com.br.mercadolivreteste

import caramelo.com.br.mercadolivreteste.model.Bank
import caramelo.com.br.mercadolivreteste.model.Installment
import caramelo.com.br.mercadolivreteste.model.PaymentMethod
import com.google.gson.Gson

object ModelMock {

    private inline fun <reified T> mockJson(file: String): T {
        val content = javaClass.classLoader?.getResourceAsStream(file)
                ?.reader()?.readText()
        return Gson().fromJson(content, T::class.java)
    }

    val INSTALLMENT by lazy { mockJson<Installment>("mock_installment.json") }

    val PAYMENT_METHODS by lazy { mockJson<List<PaymentMethod>>("mock_payment_methods.json") }

    val BANK_LIST by lazy { mockJson<List<Bank>>("mock_bank.json") }
}