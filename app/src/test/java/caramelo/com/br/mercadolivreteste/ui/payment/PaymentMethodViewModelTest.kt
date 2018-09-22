package caramelo.com.br.mercadolivreteste.ui.payment

import android.arch.lifecycle.Observer
import caramelo.com.br.mercadolivreteste.BaseUnitTest
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.model.PaymentMethod
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*

class PaymentMethodViewModelTest : BaseUnitTest() {

    @Mock
    private lateinit var payment: Payment

    @Mock
    private lateinit var repository: PaymentRepository

    @Mock
    private lateinit var observerState: Observer<PaymentMethodState>

    private lateinit var viewModel: PaymentMethodViewModel

    private val paymentMethodListMock = mutableListOf<PaymentMethod>().apply {
        add(PaymentMethod("1", "", "", "", ""))
        add(PaymentMethod("2", "", "", "", ""))
        add(PaymentMethod("3", "", "", "", ""))
    }

    @Before
    fun setUp() {
        viewModel = spy(PaymentMethodViewModel(payment, repository))
        viewModel.enableUnitTest()
        viewModel.state.observeForever(observerState)
    }

    @Test
    fun `when activity created should disable the next button and request payment methods`() {

        doNothing().whenever(viewModel).requestPaymentMethods()

        viewModel.initialize()

        val argumentCaptor = ArgumentCaptor.forClass(PaymentMethodState::class.java)
        val expectedButtonState = PaymentMethodState.Changes.NextButton(false)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            verify(viewModel, times(1)).requestPaymentMethods()
            val (buttonState) = allValues
            assertEquals(buttonState, expectedButtonState)
        }
    }

    @Test
    fun `when activity created and have a state should not disable the next button and not request payment methods`() {

        viewModel.buttonState.value = PaymentMethodState.Changes.NextButton(false)
        viewModel.listState.value = PaymentMethodState.Received.PaymentMethods(paymentMethodListMock)

        viewModel.initialize()

        val argumentCaptor = ArgumentCaptor.forClass(PaymentMethodState::class.java)

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
            verify(viewModel, never()).requestPaymentMethods()
        }
    }

    @Test
    fun `should bind the list when received the payment methods`() = runBlocking {

        doReturn(paymentMethodListMock).whenever(repository).creditCardPaymentMethods()

        viewModel.requestPaymentMethods()

        val argumentCaptor = ArgumentCaptor.forClass(PaymentMethodState::class.java)
        val expectedShowLoadingState = PaymentMethodState.Loading(true)
        val expectedReceivedState = PaymentMethodState.Received.PaymentMethods(paymentMethodListMock)
        val expectedReceivedErrorState = PaymentMethodState.Error()
        val expectedHideLoadingState = PaymentMethodState.Loading(false)

        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(capture())
            val (showLoading, receivedData, hideLoading) = allValues
            assertEquals(showLoading, expectedShowLoadingState)
            assertEquals(receivedData, expectedReceivedState)
            assertNotEquals(receivedData, expectedReceivedErrorState)
            assertEquals(hideLoading, expectedHideLoadingState)
        }
    }

    @Test
    fun `should show error when cannot received the payment methods`() = runBlocking {

        doThrow(RequestException::class.java).whenever(repository).creditCardPaymentMethods()

        viewModel.requestPaymentMethods()

        val argumentCaptor = ArgumentCaptor.forClass(PaymentMethodState::class.java)
        val expectedShowLoadingState = PaymentMethodState.Loading(true)
        val expectedReceivedState = PaymentMethodState.Received.PaymentMethods(paymentMethodListMock)
        val expectedReceivedErrorState = PaymentMethodState.Error()
        val expectedHideLoadingState = PaymentMethodState.Loading(false)

        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(capture())
            val (showLoading, receivedData, hideLoading) = allValues
            assertEquals(showLoading, expectedShowLoadingState)
            assertNotEquals(receivedData, expectedReceivedState)
            assertEquals(receivedData.javaClass, expectedReceivedErrorState.javaClass)
            assertEquals(hideLoading, expectedHideLoadingState)
        }
    }

    @Test
    fun `verify when set payment method in payment class`() {

        viewModel.paymentMethodList.apply {
            clear()
            addAll(paymentMethodListMock)
        }

        viewModel.setPaymentMethod("2")

        val argumentCaptor = ArgumentCaptor.forClass(PaymentMethodState::class.java)
        val expectedButtonState = PaymentMethodState.Changes.NextButton(true)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (buttonState) = allValues
            assertEquals(buttonState, expectedButtonState)
        }
    }

}