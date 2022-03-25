package com.merseyside.adapters.ext

import com.merseyside.adapters.delegates.composites.CompositeAdapter
import com.merseyside.adapters.model.AdapterParentViewModel

inline fun <reified R, Parent> CompositeAdapter<Parent, *>.filterIsInstance(): List<R>
        where R : AdapterParentViewModel<out Parent, Parent> {
    return modelList.filterIsInstance<R>()
}

inline fun <reified R, Parent> CompositeAdapter<Parent, *>.findIsInstance(): R?
        where R : AdapterParentViewModel<out Parent, Parent> {
    return filterIsInstance().firstOrNull() as? R
}