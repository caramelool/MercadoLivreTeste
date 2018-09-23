package caramelo.com.br.mercadolivreteste.ui.installment

import android.arch.lifecycle.Observer
import caramelo.com.br.mercadolivreteste.BaseUnitTest
import caramelo.com.br.mercadolivreteste.ModelMock
import caramelo.com.br.mercadolivreteste.model.Installment
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import org.mockito.Mock
import caramelo.com.br.mercadolivreteste.ui.installment.InstallmentViewModel.State
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import java.lang.IllegalStateException

class InstallmentViewModelTest : BaseUnitTest() {

    private val payment = Payment()

    @Mock
    private lateinit var repository: PaymentRepository

    @Mock
    private lateinit var observerState: Observer<State>

    private val installment: Installment = ModelMock.INSTALLMENT

    private lateinit var viewModel: InstallmentViewModel

    @Before
    fun setUp() {
        viewModel = Mockito.spy(InstallmentViewModel(payment, repository))
        viewModel.enableUnitTest()
        viewModel.state.observeForever(observerState)
    }

    @Test
    fun `should show installment info when request installment successfully`() = runBlocking {
        doReturn(installment).whenever(repository).installment(payment)

        viewModel.requestInstallment()

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedShowLoading = State.Layout.Loading(true)
        val expectedInstallment = State.Received.Info(installment)
        val expectedHideLoading = State.Layout.Loading(false)

        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(argumentCaptor.capture())
            val (showLoading, info, hideLoading) = allValues
            assertEquals(showLoading, expectedShowLoading)
            assertEquals(info, expectedInstallment)
            assertEquals(hideLoading, expectedHideLoading)
        }
    }

    @Test
    fun `should set installment in payment class when select a installment`() {
        val installments = 1

        val message = installment.payerCosts
                .asSequence()
                .filter { it.installments == installments }
                .map { it.recommendedMessage }
                .firstOrNull() ?: throw IllegalStateException("payerCosts not found")

        viewModel.installment = installment
        viewModel.setInstallments(installments)

        assertEquals(payment.instalmments, installments)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedInstallmentText = State.Layout.InstallmentText(message)
        val expectedPayButton = State.Layout.PayButton(true)

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(argumentCaptor.capture())
            val (installmentText, payButton) = allValues
            assertEquals(installmentText, expectedInstallmentText)
            assertEquals(payButton, expectedPayButton)
        }
    }
}