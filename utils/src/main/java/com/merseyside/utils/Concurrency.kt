package com.merseyside.utils

import android.os.Handler
import android.os.Looper
import com.merseyside.merseyLib.time.TimeUnit
import com.merseyside.utils.ext.toHandlerCanceller

fun mainThread(onMain: () -> Unit): Handler {
    val handler = Handler(Looper.getMainLooper())
    handler.post(onMain)
    return handler
}

fun mainThreadIfNeeds(onMain: () -> Unit): Handler? {
    return if (!isMainThread()) {
        mainThread(onMain)
    } else {
        onMain.invoke()
        null
    }
}

fun runThread(onThread: () -> Unit): Thread {
    return Thread { onThread() }.apply { start() }
}

fun delayedMainThread(delay: TimeUnit, runnable: Runnable): CancellableHandler {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed(runnable, delay.millis)
    return handler.toHandlerCanceller(runnable)
}

fun delayedMainThread(delay: TimeUnit, onMain: () -> Unit): CancellableHandler {
    return delayedMainThread(delay, Runnable { onMain.invoke() })
}

fun delayedThread(delay: TimeUnit, runnable: Runnable): CancellableHandler {
    val handler = Handler()
    handler.postDelayed(runnable, delay.millis)
    return handler.toHandlerCanceller(runnable)
}

fun delayedThread(delay: TimeUnit, onThread: () -> Unit): CancellableHandler {
    return delayedThread(delay, Runnable { onThread.invoke() })
}

fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}
