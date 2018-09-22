package caramelo.com.br.mercadolivreteste.ui.amount

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides

@Module
class AmountModule {
    @Provides
    fun provideActivity(activity: AmountActivity): Activity {
        return activity
    }

    @Provides
    fun provideViewModel(activity: AmountActivity): AmountViewModel {
        return ViewModelProviders.of(activity)[AmountViewModel::class.java]
    }
}