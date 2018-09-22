package caramelo.com.br.mercadolivreteste.ui.bank

import android.arch.lifecycle.Observer
import caramelo.com.br.mercadolivreteste.BaseUnitTest
import caramelo.com.br.mercadolivreteste.extension.RequestException
import caramelo.com.br.mercadolivreteste.model.Bank
import caramelo.com.br.mercadolivreteste.model.Payment
import caramelo.com.br.mercadolivreteste.repository.PaymentRepository
import caramelo.com.br.mercadolivreteste.ui.bank.BankViewModel.State
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*

class BankViewModelTest : BaseUnitTest() {

    private val payment = Payment()

    @Mock
    private lateinit var repository: PaymentRepository

    @Mock
    private lateinit var observerState: Observer<State>

    private lateinit var viewModel: BankViewModel

    private val bankListMock = mutableListOf<Bank>().apply {
        add(Bank("1", "", "", ""))
        add(Bank("2", "", "", ""))
        add(Bank("3", "", "", ""))
    }

    @Before
    fun setUp() {
        viewModel = spy(BankViewModel(payment, repository))
        viewModel.enableUnitTest()
        viewModel.state.observeForever(observerState)
    }

    @Test
    fun `when activity created should disable the next button and request bank list`() {

        doNothing().whenever(viewModel).requestBanks()

        viewModel.initialize()

        verify(viewModel, times(1)).requestBanks()

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedButtonState = State.Layout.NextButton(false)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (buttonState) = allValues
            assertEquals(buttonState, expectedButtonState)
        }
    }

    @Test
    fun `when activity created and have a state should not disable the next button and not request bank list`() {

        viewModel.buttonState.value = State.Layout.NextButton(false)
        viewModel.listState.value = State.Received.Banks(bankListMock)

        viewModel.initialize()

        verify(viewModel, never()).requestBanks()

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)

        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(capture())
        }
    }

    @Test
    fun `should show empty view when received a bank list empty`() = runBlocking {

        doReturn(emptyList<Bank>()).whenever(repository).banks(payment.methodId)

        viewModel.requestBanks()

        verify(repository, times(1)).banks(payment.methodId)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedShowLoadingState = State.Layout.Loading(true)
        val expectedReceivedState = State.Received.Empty
        val expectedHideLoadingState = State.Layout.Loading(false)

        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(capture())

            val (showLoading, receivedData, hideLoading) = allValues
            assertEquals(showLoading, expectedShowLoadingState)
            assertEquals(receivedData, expectedReceivedState)
            assertEquals(hideLoading, expectedHideLoadingState)
        }
    }

    @Test
    fun `should bind the list when received the bank list`() = runBlocking {

        doReturn(bankListMock).whenever(repository).banks(payment.methodId)

        viewModel.requestBanks()

        verify(repository, times(1)).banks(payment.methodId)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedShowLoadingState = State.Layout.Loading(true)
        val expectedReceivedState = State.Received.Banks(bankListMock)
        val expectedHideLoadingState = State.Layout.Loading(false)

        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(capture())
            val (showLoading, receivedData, hideLoading) = allValues
            assertEquals(showLoading, expectedShowLoadingState)
            assertEquals(receivedData, expectedReceivedState)
            assertEquals(hideLoading, expectedHideLoadingState)
        }
    }

    @Test
    fun `should show error when cannot received the bank list`() = runBlocking {

        doThrow(RequestException::class.java).whenever(repository).banks(payment.methodId)

        viewModel.requestBanks()

        verify(repository, times(1)).banks(payment.methodId)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedShowLoadingState = State.Layout.Loading(true)
        val expectedReceivedState = State.Received.Error
        val expectedHideLoadingState = State.Layout.Loading(false)

        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(capture())
            val (showLoading, receivedData, hideLoading) = allValues
            assertEquals(showLoading, expectedShowLoadingState)
            assertEquals(receivedData, expectedReceivedState)
            assertEquals(hideLoading, expectedHideLoadingState)
        }
    }

    @Test
    fun `verify when set bank id in payment class`() {
        viewModel.setBank("2")

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedButtonState = State.Layout.NextButton(true)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (buttonState) = allValues
            assertEquals(payment.bankId, "2")
            assertEquals(buttonState, expectedButtonState)
        }
    }

}