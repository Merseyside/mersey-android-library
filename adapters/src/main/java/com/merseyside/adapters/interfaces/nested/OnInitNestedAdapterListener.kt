package com.merseyside.adapters.interfaces.nested

import com.merseyside.adapters.base.BaseAdapter


interface OnInitNestedAdapterListener<Data> {
    fun onInitNestedAdapter(adapter: BaseAdapter<Data, *>)
}