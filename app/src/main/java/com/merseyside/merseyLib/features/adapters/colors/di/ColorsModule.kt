package com.merseyside.merseyLib.features.adapters.colors.di

import android.app.Application
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.merseyLib.features.adapters.colors.model.ColorsViewModel
import com.merseyside.merseyLib.features.adapters.colors.producer.ColorProducer
import com.merseyside.merseyLib.features.adapters.colors.view.ColorsFragment
import dagger.Module
import dagger.Provides

@Module
class ColorsModule(private val fragment: ColorsFragment) {

    @Provides
    fun provideNewsViewModel(
        @ApplicationContext application: Application,
        colorProducer: ColorProducer,
        testInteractor: TestInteractor
    ) = fragment.viewModel { ColorsViewModel(application, colorProducer, testInteractor) }

    @Provides
    fun provideColorProducer(): ColorProducer {
        return ColorProducer()
    }

    @Provides
    fun provideTestInteractor(): TestInteractor {
        return TestInteractor()
    }
}