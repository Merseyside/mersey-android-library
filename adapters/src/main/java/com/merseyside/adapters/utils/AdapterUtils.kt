@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.utils

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.adapters.feature.filter.FilterFeature
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.interfaces.base.IBaseAdapter
import com.merseyside.adapters.model.AdapterParentViewModel

internal fun BaseAdapter<*, *>.isFilterable(): Boolean {
    return adapter is Filterable<*, *>
}

internal fun <Parent, Model : AdapterParentViewModel<out Parent, Parent>>
        BaseAdapter<Parent, Model>.getFilter(): FilterFeature<Parent, Model> {
    return (adapter as Filterable<Parent, Model>).filter.apply {
        workManager = this@getFilter.workManager
    }
}