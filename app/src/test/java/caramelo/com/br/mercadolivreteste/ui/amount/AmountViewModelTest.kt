package caramelo.com.br.mercadolivreteste.ui.amount

import android.arch.lifecycle.Observer
import caramelo.com.br.mercadolivreteste.BaseUnitTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*

class AmountViewModelTest : BaseUnitTest() {

    @Mock
    private lateinit var observerState: Observer<AmountState>

    private lateinit var viewModel: AmountViewModel

    @Before
    fun setUp() {
        viewModel = spy(AmountViewModel())
        viewModel.state.observeForever(observerState)
    }

    @Test
    fun `when value was more then 0 should enable button`() {
        viewModel.amount = 100f

        val argumentCaptor = ArgumentCaptor.forClass(AmountState::class.java)
        val expectedState = AmountState.Layout.NextButton(true)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (state) = allValues
            assertEquals(state, expectedState)
        }
    }

    @Test
    fun `when value was less or equals 0 should disable button`() {
        viewModel.amount = 0f

        val argumentCaptor = ArgumentCaptor.forClass(AmountState::class.java)
        val expectedState = AmountState.Layout.NextButton(false)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (state) = allValues
            assertEquals(state, expectedState)
        }
    }

    @Test
    fun `check if the payment value is equals value variable in presenter`() {
        viewModel.amount = 100f
        assert(viewModel.payment.amount == viewModel.amount)
    }
}