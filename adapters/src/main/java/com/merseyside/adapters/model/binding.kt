package com.merseyside.adapters.model

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.SelectableAdapter
import com.merseyside.adapters.callback.OnItemSelectedListener

@BindingAdapter("onSelected")
fun setSelectedListener(
    view: RecyclerView,
    oldListener: OnItemSelectedListener<Any>?,
    newListener: OnItemSelectedListener<Any>?
) {
    val adapter = view.adapter as SelectableAdapter<Any, *>

    if (newListener != null) {
        adapter.addOnItemSelectedListener(newListener)
    }
}