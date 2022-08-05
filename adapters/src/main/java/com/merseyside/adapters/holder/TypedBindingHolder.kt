package com.merseyside.adapters.holder

import androidx.databinding.ViewDataBinding
import com.merseyside.adapters.model.AdapterParentViewModel

open class TypedBindingHolder<T: AdapterParentViewModel<*, *>>(binding: ViewDataBinding)
    : BaseBindingHolder(binding) {

    @Suppress("UNCHECKED_CAST")
    override fun getModel(): T {
        return super.getModel() as T
    }
}
