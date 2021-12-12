package com.merseyside.archy.domain.interactor.coroutines

import com.merseyside.merseyLib.kotlin.Logger
import kotlinx.coroutines.*

abstract class CoroutineNoResultUseCase<Params>: BaseCoroutineUseCase<Unit, Params>() {

    fun execute(
        coroutineScope: CoroutineScope = mainScope,
        onPreExecute: () -> Unit = {},
        onComplete: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onPostExecute: () -> Unit = {},
        params: Params? = null
    ) {
        coroutineScope.launch {
            onPreExecute()

            val deferred = doWorkAsync(params)

            try {
                deferred.await()
                onComplete.invoke()
            } catch (throwable: CancellationException) {
                Logger.log(this, "The coroutine had canceled")
            } catch (throwable: Throwable) {
                Logger.logErr(throwable)
                onError(throwable)
            }

            onPostExecute()
        }
    }
}