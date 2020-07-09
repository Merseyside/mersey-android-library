package com.merseyside.archy.presentation.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.merseyside.archy.presentation.model.BaseViewModel
import com.merseyside.archy.presentation.dispatcher.EventsDispatcherOwner

abstract class BaseVMEventsActivity<B: ViewDataBinding, M, Listener : Any>
    : BaseVMActivity<B, M>() where M : BaseViewModel, M: EventsDispatcherOwner<Listener> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        viewModel.eventsDispatcher.bind(this, this as Listener)
    }
}