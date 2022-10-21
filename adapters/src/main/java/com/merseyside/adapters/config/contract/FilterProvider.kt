package com.merseyside.adapters.config.contract

import com.merseyside.adapters.feature.filter.AdapterFilter
import com.merseyside.adapters.model.VM

interface FilterProvider<Parent, Model>
        where Model : VM<Parent> {

    val adapterFilter: AdapterFilter<Parent, Model>
}