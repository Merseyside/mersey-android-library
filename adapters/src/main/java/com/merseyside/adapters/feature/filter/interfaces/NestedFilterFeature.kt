package com.merseyside.adapters.feature.filter.interfaces

import com.merseyside.adapters.model.NestedAdapterParentViewModel

abstract class NestedFilterFeature<Parent, Model : NestedAdapterParentViewModel<out Parent, Parent, *>> :
    FilterFeature<Parent, Model>() {

    internal lateinit var getFilterableByModel: (Model) -> Filterable<*, *>?



}