package com.merseyside.adapters.interfaces.nested

interface HasNestedAdapterListener<InnerData> {

    var onInitAdapterListener: OnInitNestedAdapterListener<InnerData>?

    fun setInitNestedAdapterListener(listener: OnInitNestedAdapterListener<InnerData>) {
        onInitAdapterListener = listener
    }
}