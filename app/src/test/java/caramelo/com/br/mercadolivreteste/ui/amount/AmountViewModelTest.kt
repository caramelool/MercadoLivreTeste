package caramelo.com.br.mercadolivreteste.ui.amount

import android.arch.lifecycle.Observer
import caramelo.com.br.mercadolivreteste.BaseUnitTest
import caramelo.com.br.mercadolivreteste.ui.amount.AmountViewModel.State
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*

class AmountViewModelTest : BaseUnitTest() {

    @Mock
    private lateinit var observerState: Observer<State>

    private lateinit var viewModel: AmountViewModel

    @Before
    fun setUp() {
        viewModel = spy(AmountViewModel())
        viewModel.state.observeForever(observerState)
    }

    @Test
    fun `when value was more then 0 should enable button`() {
        viewModel.setAmount(100f)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedState = State.Layout.NextButton(true)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (state) = allValues
            assertEquals(state, expectedState)
        }
    }

    @Test
    fun `when value was less or equals 0 should disable button`() {
        viewModel.setAmount(0f)

        val argumentCaptor = ArgumentCaptor.forClass(State::class.java)
        val expectedState = State.Layout.NextButton(false)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (state) = allValues
            assertEquals(state, expectedState)
        }
    }

    @Test
    fun `check if the payment value is equals value variable in presenter`() {
        val amount = 100f
        viewModel.setAmount(amount)
        assert(viewModel.payment.amount == amount)
    }
}