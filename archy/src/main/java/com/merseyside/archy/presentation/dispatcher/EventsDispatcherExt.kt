package com.merseyside.archy.presentation.dispatcher

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

fun createExecutorOnMainLooper(): Executor {
    val mainLooper = Looper.getMainLooper()
    val mainHandler = Handler(mainLooper)
    return Executor { mainHandler.post(it) }
}

inline fun <reified T : Any> eventsDispatcherOnMain(): EventsDispatcher<T> {
    return EventsDispatcher(createExecutorOnMainLooper())
}