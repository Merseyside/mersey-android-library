package com.merseyside.archy.presentation.dispatcher

interface EventsDispatcherOwner<T : Any> {
    val eventsDispatcher: EventsDispatcher<T>
}