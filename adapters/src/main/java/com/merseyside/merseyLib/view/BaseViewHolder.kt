package com.merseyside.merseyLib.view

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(variable: Int, obj: Any) {
        binding.apply {
            setVariable(variable, obj)
            executePendingBindings()
        }
    }

    val context: Context
        get() = itemView.context

}
