package caramelo.com.br.mercadolivreteste.ui.value

import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ValuePresenterTest {

    @Mock
    lateinit var view: ValueView

    lateinit var presenter: ValuePresenter

    @Before
    fun setUp() {
        presenter = spy(ValuePresenter(view))
    }

    @Test
    fun `when value was more then 0 should enable button`() {
        presenter.value = 100f

        verify(view, times(1)).enableButton()
        verify(view, never()).disableButton()
    }

    @Test
    fun `when value was less or equals 0 should disable button`() {
        presenter.value = 0f

        verify(view, never()).enableButton()
        verify(view, times(1)).disableButton()
    }

    @Test
    fun `check if the payment value is equals value variable in presenter`() {
        presenter.value = 100f
        assert(presenter.payment.value == presenter.value)
    }
}