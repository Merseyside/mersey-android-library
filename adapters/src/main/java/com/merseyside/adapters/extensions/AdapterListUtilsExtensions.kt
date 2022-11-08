package com.merseyside.adapters.extensions

import com.merseyside.adapters.interfaces.base.IBaseAdapter

inline fun <reified R: Parent, Parent> IBaseAdapter<Parent, *>.filterItemsIsInstance(): List<R> {
    return getAll().filterIsInstance<R>()
}

inline fun <reified R: Parent, Parent> IBaseAdapter<Parent, *>.findItemsIsInstance(): R {
    return filterItemsIsInstance<R, Parent>().first()
}