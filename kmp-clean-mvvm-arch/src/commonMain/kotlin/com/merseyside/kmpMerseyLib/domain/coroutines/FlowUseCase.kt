package com.merseyside.kmpMerseyLib.domain.coroutines

import com.merseyside.kmpMerseyLib.utils.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

abstract class FlowUseCase<T, Params> : CoroutineScope by CoroutineScope(applicationContext) {

    var job: Job? = null

    var backgroundContext: CoroutineContext = computationContext

    @ExperimentalCoroutinesApi
    protected abstract fun executeOnBackground(params: Params?): Flow<T>

    fun observe(
        params: Params? = null,
        onEmit: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        val flow = executeOnBackground(params)
            .flowOn(backgroundContext)

        if (job != null) {
            cancel()
        }

        job = launch {
            try {
                flow.collect { data ->
                    onEmit.invoke(data)
                }
            } catch (e: CancellationException) {
                Logger.log(this, "Coroutine had canceled")
            }
            catch (e: Throwable) {
                Logger.log(e)
                onError.invoke(e)
            }
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }
}