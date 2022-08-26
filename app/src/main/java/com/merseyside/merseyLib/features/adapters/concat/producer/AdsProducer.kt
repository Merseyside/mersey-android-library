package com.merseyside.merseyLib.features.adapters.concat.producer

import com.merseyside.merseyLib.features.adapters.concat.entity.Ads
import com.merseyside.merseyLib.time.coroutines.delay
import com.merseyside.merseyLib.time.units.TimeUnit
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.CoroutineContext

class AdsProducer: CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private var producerJob: Job? = null

    private val adsFlow = MutableSharedFlow<Ads>()

    var id: Int = 0
    fun getAdsFlow() : Flow<Ads> = adsFlow

    fun startProduceAds(delay: TimeUnit) {
        producerJob = launch {
            while(isActive) {
                delay(delay)

                adsFlow.emit(Ads(id))
                id++
            }
        }
    }

    fun stopProducer() {
        producerJob?.cancel()
    }
}