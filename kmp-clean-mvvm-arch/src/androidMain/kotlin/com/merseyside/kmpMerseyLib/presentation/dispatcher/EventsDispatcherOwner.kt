package com.merseyside.kmpMerseyLib.presentation.dispatcher

interface EventsDispatcherOwner<T : Any> {
    val eventsDispatcher: EventsDispatcher<T>
}