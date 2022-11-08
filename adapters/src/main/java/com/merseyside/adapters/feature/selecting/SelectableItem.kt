package com.merseyside.adapters.feature.selecting

interface SelectableItem {

    val selectState: SelectState

    fun isSelected(): Boolean = selectState.selected
}