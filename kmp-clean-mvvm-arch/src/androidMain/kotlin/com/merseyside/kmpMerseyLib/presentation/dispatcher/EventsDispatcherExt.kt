package com.merseyside.kmpMerseyLib.presentation.dispatcher

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

fun createExecutorOnMainLooper(): Executor {
    val mainHandler = Handler(Looper.getMainLooper())
    return Executor { mainHandler.post(it) }
}

inline fun <reified T : Any> eventsDispatcherOnMain(): EventsDispatcher<T> {
    return EventsDispatcher(createExecutorOnMainLooper())
}