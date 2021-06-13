package com.merseyside.merseyLib.features.adapters.concat.di

import android.app.Application
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.merseyLib.features.adapters.concat.model.ConcatViewModel
import com.merseyside.merseyLib.features.adapters.concat.producer.AdsProducer
import com.merseyside.merseyLib.features.adapters.concat.producer.NewsProducer
import com.merseyside.merseyLib.features.adapters.concat.view.ConcatAdapterFragment
import dagger.Module
import dagger.Provides

@Module
class ConcatModule(private val fragment: ConcatAdapterFragment) {

    @Provides
    fun provideConcatViewModel(
        @ApplicationContext application: Application,
        newsProducer: NewsProducer,
        adsProducer: AdsProducer
    ) = fragment.viewModel { ConcatViewModel(application, newsProducer, adsProducer) }

    @Provides
    fun provideNewsProducer() = NewsProducer()

    @Provides
    fun provideAdsProducer() = AdsProducer()
}