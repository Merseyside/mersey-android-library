package com.merseyside.archy.domain.interactor.coroutines

import kotlinx.coroutines.*

abstract class BaseCoroutineUseCase<T, Params> {

    protected val mainScope: CoroutineScope by lazy { CoroutineScope(applicationContext) }

    private val asyncJob = SupervisorJob()
    private val scope = CoroutineScope(asyncJob)

    var job: Job? = null
        set(value) {
            field?.let {
                if (it.isActive) {
                    it.cancel()
                }
            }

            field = value
        }

    val isActive: Boolean
        get() { return job?.isActive ?: false }

    protected abstract suspend fun executeOnBackground(params: Params?): T

    protected suspend fun doWorkAsync(params: Params?): Deferred<T> = scope.backgroundAsync {
        executeOnBackground(params)
    }.also { job = it }

    fun cancel(cause: CancellationException? = null) {
        job?.cancel(cause)
        job = null
    }
}