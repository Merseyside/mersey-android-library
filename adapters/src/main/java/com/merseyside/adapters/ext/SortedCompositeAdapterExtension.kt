@file:OptIn(InternalAdaptersApi::class)

package com.merseyside.adapters.ext

import com.merseyside.adapters.delegates.composites.SortedCompositeAdapter
import com.merseyside.adapters.model.ComparableAdapterParentViewModel
import com.merseyside.adapters.utils.InternalAdaptersApi

inline fun <reified R, Parent> SortedCompositeAdapter<Parent, *>.filterIsInstance(): List<R>
        where R : ComparableAdapterParentViewModel<out Parent, Parent> {
    return modelList.filterIsInstance<R>()
}

inline fun <reified R, Parent> SortedCompositeAdapter<Parent, *>.findIsInstance(): R?
        where R : ComparableAdapterParentViewModel<out Parent, Parent> {
    return filterIsInstance().firstOrNull() as? R
}