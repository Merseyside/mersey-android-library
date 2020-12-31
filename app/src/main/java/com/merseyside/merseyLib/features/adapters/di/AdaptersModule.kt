package com.merseyside.merseyLib.features.adapters.di

import android.app.Application
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.merseyLib.features.adapters.model.AdaptersViewModel
import com.merseyside.merseyLib.features.adapters.producer.ColorProducer
import com.merseyside.merseyLib.features.adapters.view.AdaptersFragment
import dagger.Module
import dagger.Provides

@Module
class AdaptersModule(private val fragment: AdaptersFragment) {

    @Provides
    fun provideNewsViewModel(
        @ApplicationContext application: Application,
        colorProducer: ColorProducer
    ) = fragment.viewModel { AdaptersViewModel(application, colorProducer) }

    @Provides
    fun provideColorProducer(): ColorProducer {
        return ColorProducer()
    }
}