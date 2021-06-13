package com.merseyside.merseyLib.features.adapters.concat.producer

import com.merseyside.merseyLib.features.adapters.concat.entity.News
import com.merseyside.utils.time.TimeUnit
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.CoroutineContext
import com.merseyside.utils.ext.delay

class NewsProducer: CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private var producerJob: Job? = null

    private val newsFlow = MutableSharedFlow<News>()

    var id: Int = 0
    fun getNewsFlow() : Flow<News> = newsFlow

    fun startProduceNews(delay: TimeUnit) {
        producerJob = launch {
            while(isActive) {
                delay(delay)

                newsFlow.emit(News(id))
                id++
            }
        }
    }

    fun stopProducer() {
        producerJob?.cancel()
    }
}