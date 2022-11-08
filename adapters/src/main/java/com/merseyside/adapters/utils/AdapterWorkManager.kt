package com.merseyside.adapters.utils

import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlinx.coroutines.Job

class AdapterWorkManager(
    val workManager: CoroutineQueue<Any, Unit>,
    private val errorHandler: (Exception) -> Unit
) {

    fun <Result> doAsync(
        onComplete: (Result) -> Unit = {},
        onError: ((e: Exception) -> Unit)? = null,
        work: suspend () -> Result,
    ): Job? {
        return workManager.addAndExecute {
            try {
                onComplete(work())
            } catch (e: Exception) {
                Logger.logErr("AdapterWorkManager", e)
                onError?.invoke(e) ?: errorHandler(e)
            }
        }
    }
}