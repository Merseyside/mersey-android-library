package com.merseyside.kmpMerseyLib.domain.coroutines

import com.merseyside.kmpMerseyLib.utils.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class CoroutineNoResultUseCase<Params> : BaseCoroutineUseCase<Unit, Params>() {

    fun execute(
        coroutineScope: CoroutineScope = mainScope,
        onPreExecute: () -> Unit = {},
        onComplete: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onPostExecute: () -> Unit = {},
        params: Params? = null
    ) {
        if (job != null) {
            job!!.cancel()
        }

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