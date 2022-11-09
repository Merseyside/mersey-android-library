package com.merseyside.adapters.interfaces.delegate

import com.merseyside.adapters.model.ComparableAdapterParentViewModel

interface IPrioritizedDelegateAdapter<Item : Parent, Parent, Model>
    : IDelegateAdapter<Item, Parent, Model>
        where Model : ComparableAdapterParentViewModel<Item, Parent> {

    var priority: Int
}