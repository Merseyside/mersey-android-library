package com.merseyside.adapters.utils

import androidx.collection.ArraySet
import com.merseyside.adapters.config.contract.HasAdapterWorkManager
import com.merseyside.merseyLib.kotlin.coroutines.queue.CoroutineQueue
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.Job
import com.merseyside.merseyLib.kotlin.coroutines.queue.ext.executeIfIdleAsync

class AdapterWorkManager(
    val coroutineQueue: CoroutineQueue<Any, Unit>,
    private val errorHandler: (Exception) -> Unit
) {

    private val subWorkManagers = ArraySet<AdapterWorkManager>()

    fun <Result, T: HasAdapterWorkManager> subTaskWith(
        adapter: T,
        block: suspend T.() -> Result
    ) {
        val subWorkManager = adapter.workManager
        subWorkManagers.add(subWorkManager)
        subWorkManager.add { block(adapter) }
    }

    fun <Result> doAsync(
        onComplete: (Result) -> Unit = {},
        onError: ((e: Exception) -> Unit)? = null,
        work: suspend () -> Result,
    ): Job? {
        add(onComplete, onError, work)
        return coroutineQueue.executeIfIdleAsync()
    }

    private suspend fun executeIfIdle() {
        return coroutineQueue.executeIfIdle()
    }

    fun <Result> add(
        onComplete: (Result) -> Unit = {},
        onError: ((e: Exception) -> Unit)? = null,
        work: suspend () -> Result
    ) {
        coroutineQueue.add {
            try {
                val result = work()
                if (subWorkManagers.isNotEmpty()) {
                    subWorkManagers.forEach { manager ->
                        manager.executeIfIdle()
                    }
                }
                onComplete(result)
            } catch(e: Exception) {
                Logger.logErr("AdapterWorkManager", e)
                onError?.invoke(e) ?: errorHandler(e)
            }
        }
    }
}