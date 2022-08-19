package com.merseyside.adapters.listDelegates.interfaces

import com.merseyside.adapters.interfaces.nested.AdapterNestedListActions
import com.merseyside.adapters.listDelegates.PrioritizedListChangeDelegate
import com.merseyside.adapters.model.NestedAdapterParentViewModel

class NestedListChangeDelegate<Parent,
        Model : NestedAdapterParentViewModel<out Parent, Parent, *>, InnerAdapter>(
    override val listActions: AdapterNestedListActions<Parent, Model, InnerAdapter>
) : PrioritizedListChangeDelegate<Parent, Model>(listActions), AdapterNestedListChangeDelegate<Parent, Model, InnerAdapter> {


}