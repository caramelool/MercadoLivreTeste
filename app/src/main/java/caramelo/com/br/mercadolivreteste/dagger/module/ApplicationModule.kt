package caramelo.com.br.mercadolivreteste.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(
    private val context: Context
) {
    @Provides
    fun provideContext() = context
}