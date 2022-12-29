package com.merseyside.adapters.utils

import androidx.collection.ArraySet
import com.merseyside.adapters.config.contract.HasAdapterWorkManager
import com.merseyside.merseyLib.kotlin.coroutines.queue.CoroutineQueue
import com.merseyside.merseyLib.kotlin.coroutines.queue.ext.executeAsync
import com.merseyside.merseyLib.kotlin.coroutines.utils.CompositeJob
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.Job

class AdapterWorkManager(
    private val coroutineQueue: CoroutineQueue<Any, Unit>,
    private val errorHandler: (Exception) -> Unit
) {

    private val subManagers = ArraySet<AdapterWorkManager>()
    private val subCompositeJob: CompositeJob = CompositeJob()

    fun <Result, T: HasAdapterWorkManager> subTaskWith(
        adapter: T,
        block: suspend T.() -> Result
    ) {
        val subWorkManager = adapter.workManager
        subManagers.add(subWorkManager)
        subWorkManager.add { block(adapter) }
    }

    fun <Result> doAsync(
        onComplete: (Result) -> Unit = {},
        onError: ((e: Exception) -> Unit)? = null,
        work: suspend () -> Result,
    ): Job? {
        add(onComplete, onError, work)
        return coroutineQueue.executeAsync()
    }

    private fun executeAsync(): Job? {
        return coroutineQueue.executeAsync()
    }

    fun <Result> add(
        onComplete: (Result) -> Unit = {},
        onError: ((e: Exception) -> Unit)? = null,
        work: suspend () -> Result
    ) {
        coroutineQueue.add {
            try {
                val result = work()
                if (subManagers.isNotEmpty()) {
                    subManagers.forEach { manager ->
                        val job = manager.executeAsync()
                        if (job != null) subCompositeJob.add(job)
                    }

                    subCompositeJob.joinAll()
                }
                onComplete(result)
            } catch(e: Exception) {
                Logger.logErr("AdapterWorkManager", e)
                onError?.invoke(e) ?: errorHandler(e)
            }
        }
    }
}