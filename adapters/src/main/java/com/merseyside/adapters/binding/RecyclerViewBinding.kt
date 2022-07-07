package com.merseyside.adapters.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.SortedAdapter

@BindingAdapter("filter")
fun setFilter(recyclerView: RecyclerView, query: String?) {
    if (query != null && recyclerView.adapter != null) {
        val adapter = recyclerView.adapter as? SortedAdapter<*, *>
        adapter?.setFilter(query)
            ?: throw Exception("Filter attribute only works with SortedAdapter!")
    }
}

@BindingAdapter("filterAsync")
fun setAsyncFilter(recyclerView: RecyclerView, query: String?) {
    if (query != null && recyclerView.adapter != null) {
        val adapter = recyclerView.adapter as? SortedAdapter<*, *>
        adapter?.setFilterAsync(query)
            ?: throw Exception("Filter attribute only works with SortedAdapter! ${recyclerView.adapter}")
    }
}