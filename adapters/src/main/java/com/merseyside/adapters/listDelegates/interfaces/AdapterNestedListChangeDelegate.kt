package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.model.NestedAdapterParentViewModel

interface AdapterNestedListChangeDelegate<Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, *>, InnerAdapter>:
    AdapterPrioritizedListChangeDelegate<Parent, Model> {


}