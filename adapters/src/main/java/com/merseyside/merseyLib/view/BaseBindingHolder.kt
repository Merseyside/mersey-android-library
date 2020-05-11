package com.merseyside.merseyLib.view

import android.content.Context
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.merseyLib.model.BaseAdapterViewModel
import com.merseyside.merseyLib.utils.ext.getActivity

open class BaseBindingHolder<T: Any>(val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    lateinit var model: T

    @CallSuper
    fun bind(variable: Int, obj: T) {
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
