package com.merseyside.kmpMerseyLib.presentation.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.merseyside.archy.presentation.fragment.BaseVMFragment
import com.merseyside.archy.presentation.model.BaseViewModel
import com.merseyside.kmpMerseyLib.presentation.dispatcher.EventsDispatcherOwner

abstract class BaseVMEventsFragment<B: ViewDataBinding, M, Listener : Any>
    : BaseVMFragment<B, M>() where M : BaseViewModel, M: EventsDispatcherOwner<Listener> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        viewModel.eventsDispatcher.bind(this, this as Listener)
    }
}