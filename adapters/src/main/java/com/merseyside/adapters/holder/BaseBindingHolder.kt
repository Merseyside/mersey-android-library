package com.merseyside.adapters.holder

import android.content.Context
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.utils.view.ext.getActivity

open class BaseBindingHolder(val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    val isInitialized: Boolean
        get() = this::_model.isInitialized

    private lateinit var _model: Any

    open val model: Any
        get() = _model

    init {
        binding.lifecycleOwner = itemView.getActivity()
    }

    @CallSuper
    fun bind(variable: Int, obj: Any) {
        _model = obj

        binding.apply {
            setVariable(variable, obj)
            executePendingBindings()
        }
    }

    val context: Context
        get() = itemView.context
}