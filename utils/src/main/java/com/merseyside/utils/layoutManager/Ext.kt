package com.merseyside.utils.layoutManager

import androidx.recyclerview.widget.RecyclerView.LayoutManager

fun LayoutManager.findLastVisibleItemPosition(): Int {
    return when(this) {
        is LinearLayoutManager -> findLastVisibleItemPosition()
        is GridLayoutManager -> findLastVisibleItemPosition()
        else -> throw NotImplementedError("Not implemented.")
    }
}

fun LayoutManager.findFirstVisibleItemPosition(): Int {
    return when(this) {
        is LinearLayoutManager -> findFirstVisibleItemPosition()
        is GridLayoutManager -> findFirstVisibleItemPosition()
        else -> throw NotImplementedError("Not implemented.")
    }
}