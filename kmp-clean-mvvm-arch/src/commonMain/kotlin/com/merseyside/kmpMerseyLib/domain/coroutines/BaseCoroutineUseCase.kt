package com.merseyside.kmpMerseyLib.domain.coroutines

import com.merseyside.kmpMerseyLib.utils.time.TimeUnit
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseCoroutineUseCase<T, Params> {

    protected val mainScope: CoroutineScope by lazy { CoroutineScope(applicationContext) }

    var job: Job? = null

    var delay: TimeUnit? = null

    val isActive: Boolean
        get() { return job?.isActive ?: false }

    protected val backgroundContext: CoroutineContext
        get() = computationContext

    private val asyncJob = SupervisorJob()
    private val scope = CoroutineScope(backgroundContext + asyncJob)

    protected abstract suspend fun executeOnBackground(params: Params?): T

    protected suspend fun doWorkAsync(params: Params?): Deferred<T> = scope.async(backgroundContext) {
        if (delay != null) {
            delay(delay!!.toMillisLong())
        }

        executeOnBackground(params)
    }.also { job = it }

    protected suspend fun <X> background(context: CoroutineContext = backgroundContext, block: suspend () -> X): Deferred<X> {
        return scope.async(context) {
            block.invoke()
        }
    }

    fun cancel(cause: CancellationException? = null) {
        job?.cancel(cause)
        job = null
    }

}