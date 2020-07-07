package com.merseyside.adapters.view

import android.content.Context
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.utils.ext.getActivity

open class BaseBindingHolder(val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    lateinit var model: Any

    @CallSuper
    fun bind(variable: Int, obj: Any) {
        model = obj

        binding.apply {
            setVariable(variable, obj)
            executePendingBindings()

            lifecycleOwner = itemView.getActivity()
        }
    }

    val context: Context
        get() = itemView.context
}