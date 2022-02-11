package com.merseyside.merseyLib.features.adapters.concat.model

import android.app.Application
import com.merseyside.archy.presentation.model.AndroidViewModel
import com.merseyside.merseyLib.features.adapters.concat.producer.AdsProducer
import com.merseyside.merseyLib.features.adapters.concat.producer.NewsProducer
import com.merseyside.merseyLib.time.units.Seconds

class ConcatViewModel(
    application: Application,
    private val newsProducer: NewsProducer,
    private val adsProducer: AdsProducer
) : AndroidViewModel(application) {

    fun getNewsFlow() = newsProducer.getNewsFlow()
    fun getAdsFlow() = adsProducer.getAdsFlow()


    fun startProducers() {
        newsProducer.startProduceNews(Seconds(5))
        adsProducer.startProduceAds(Seconds(8))
    }

    fun stopProducer() {
        newsProducer.stopProducer()
        adsProducer.stopProducer()
    }
}