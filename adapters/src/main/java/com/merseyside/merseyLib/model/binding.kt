package com.merseyside.merseyLib.model

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.merseyLib.adapters.BaseSelectableAdapter

@BindingAdapter("onSelected")
fun setSelectedListener(
    view: RecyclerView,
    oldListener: BaseSelectableAdapter.OnItemSelectedListener<Any>?,
    newListener: BaseSelectableAdapter.OnItemSelectedListener<Any>?
) {
    val adapter = view.adapter as BaseSelectableAdapter<Any, *>

    if (newListener != null) {
        adapter.setOnItemSelectedListener(newListener)
    }
}