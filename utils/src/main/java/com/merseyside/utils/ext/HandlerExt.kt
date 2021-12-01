package com.merseyside.utils.ext

import android.os.Handler
import com.merseyside.utils.CancellableHandler

fun Handler.toHandlerCanceller(runnable: Runnable): CancellableHandler {
    return CancellableHandler(this, runnable)
}