package com.merseyside.adapters.feature.expanding

interface ExpandableItem {

    val expandState: ExpandState

    fun isExpanded(): Boolean = expandState.expdaned
}