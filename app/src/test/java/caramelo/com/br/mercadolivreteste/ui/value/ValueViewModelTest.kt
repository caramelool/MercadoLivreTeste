package caramelo.com.br.mercadolivreteste.ui.value

import android.arch.lifecycle.Observer
import caramelo.com.br.mercadolivreteste.BaseUnitTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*

class ValueViewModelTest : BaseUnitTest() {

    @Mock
    private lateinit var observerState: Observer<ValueState>

    private lateinit var viewModel: ValueViewModel

    @Before
    fun setUp() {
        viewModel = spy(ValueViewModel())
        viewModel.state.observeForever(observerState)
    }

    @Test
    fun `when value was more then 0 should enable button`() {
        viewModel.value = 100f

        val argumentCaptor = ArgumentCaptor.forClass(ValueState::class.java)
        val expectedState = ValueState.Changes.NextButton(true)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (state) = allValues
            assertEquals(state, expectedState)
        }
    }

    @Test
    fun `when value was less or equals 0 should disable button`() {
        viewModel.value = 0f

        val argumentCaptor = ArgumentCaptor.forClass(ValueState::class.java)
        val expectedState = ValueState.Changes.NextButton(false)

        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(capture())
            val (state) = allValues
            assertEquals(state, expectedState)
        }
    }

    @Test
    fun `check if the payment value is equals value variable in presenter`() {
        viewModel.value = 100f
        assert(viewModel.payment.value == viewModel.value)
    }
}