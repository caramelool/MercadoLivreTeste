package caramelo.com.br.mercadolivreteste.ui.value

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class ValueModule {
    @Provides
    fun provideActivity(activity: ValueActivity): Activity {
        return activity
    }

    @Provides
    fun provideViewModel(activity: ValueActivity): ValueViewModel {
        return ViewModelProviders.of(activity)[ValueViewModel::class.java]
    }
}