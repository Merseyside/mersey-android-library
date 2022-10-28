package com.merseyside.adapters.config.contract

import com.merseyside.merseyLib.kotlin.coroutines.CoroutineQueue
import kotlinx.coroutines.Job

interface HasWorkManager {

    val workManager: CoroutineQueue<Any, Unit>

    fun <Result> doAsync(
        onComplete: (Result) -> Unit = {},
        onError: (e: Exception) -> Unit = {},
        work: suspend () -> Result,
    ): Job? {
        return workManager.addAndExecute {
            try {
                onComplete(work())
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}