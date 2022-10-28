package com.merseyside.adapters.config.contract

import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.VM

interface OnBindItemListener<Parent, Model : VM<Parent>> {

    fun onBindViewHolder(holder: TypedBindingHolder<Model>, model: Model, position: Int)
}