package com.merseyside.adapters.view

import androidx.databinding.ViewDataBinding

open class TypedBindingHolder<T: Any>(binding: ViewDataBinding)
    : BaseBindingHolder(binding) {

    @Suppress("UNCHECKED_CAST")
    fun getObj(): T {
        return model as T
    }
}
