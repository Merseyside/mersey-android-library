package com.merseyside.adapters.model

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.BaseSelectableAdapter
import com.merseyside.adapters.base.OnItemSelectedListener

@BindingAdapter("onSelected")
fun setSelectedListener(
    view: RecyclerView,
    oldListener: OnItemSelectedListener<Any>?,
    newListener: OnItemSelectedListener<Any>?
) {
    val adapter = view.adapter as BaseSelectableAdapter<Any, *>

    if (newListener != null) {
        adapter.setOnItemSelectedListener(newListener)
    }
}