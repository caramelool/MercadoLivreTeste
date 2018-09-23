package caramelo.com.br.mercadolivreteste

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class BaseUnitTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
}