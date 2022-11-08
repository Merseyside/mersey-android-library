package com.merseyside.adapters.feature.selecting

interface OnSelectCallback {

    fun onSelect(item: SelectableItem)

    fun onSelect(item: SelectableItem, selected: Boolean)
}