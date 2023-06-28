package com.merseyside.utils.layout

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.LayoutManager.findLastVisibleItemPosition(): Int {
    return when(this) {
        is LinearLayoutManager -> findLastVisibleItemPosition()
        is GridLayoutManager -> findLastVisibleItemPosition()
        else -> throw NotImplementedError("Not implemented.")
    }
}

fun RecyclerView.LayoutManager.findFirstVisibleItemPosition(): Int {
    return when(this) {
        is LinearLayoutManager -> findFirstVisibleItemPosition()
        is GridLayoutManager -> findFirstVisibleItemPosition()
        else -> throw NotImplementedError("Not implemented.")
    }
}