package com.merseyside.merseyLib.presentation.view

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(private val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(variable: Int, obj: Any) {
        binding.setVariable(variable, obj)
        binding.executePendingBindings()
    }
}
