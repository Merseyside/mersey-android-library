package com.merseyside.adapters.feature.expanding

interface OnExpandCallback {

    fun onExpand(item: ExpandableItem)

    fun onExpand(item: ExpandableItem, expanded: Boolean)
}