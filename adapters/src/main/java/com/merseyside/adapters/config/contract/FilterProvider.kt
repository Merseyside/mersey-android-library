package com.merseyside.adapters.config.contract

import com.merseyside.adapters.feature.filter.AdapterFilter
import com.merseyside.adapters.model.AdapterParentViewModel

interface FilterProvider<Parent, Model>
        where Model : AdapterParentViewModel<out Parent, Parent> {

    val adapterFilter: AdapterFilter<Parent, Model>
}