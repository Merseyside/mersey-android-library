package com.merseyside.utils.ext

import android.os.Handler
import com.merseyside.utils.HandlerCanceller

fun Handler.toHandlerCanceller(runnable: Runnable): HandlerCanceller {
    return HandlerCanceller(this, runnable)
}