package com.merseyside.merseyLib.view

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.merseyLib.utils.ext.getActivity

class BaseViewHolder(val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(variable: Int, obj: Any) {
        binding.apply {
            setVariable(variable, obj)
            executePendingBindings()

            lifecycleOwner = itemView.getActivity()
        }
    }

    val context: Context
        get() = itemView.context

}
