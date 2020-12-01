package com.merseyside.archy.domain.interactor.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

suspend fun <X> CoroutineScope.backgroundAsync(context: CoroutineContext = computationContext, block: suspend () -> X): Deferred<X> {
    return async(context) {
        block.invoke()
    }
}

suspend fun debounce(
    waitMs: Long = 300L,
    destinationFunction: suspend () -> Unit
) {
    delay(waitMs)
    destinationFunction.invoke()
}

class DebounceException(msg: String): CancellationException(msg)