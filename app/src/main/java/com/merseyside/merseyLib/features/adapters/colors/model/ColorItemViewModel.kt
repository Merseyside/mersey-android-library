package com.merseyside.merseyLib.features.adapters.colors.model

import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor

class ColorItemViewModel(obj: HexColor) : ComparableAdapterViewModel<HexColor>(obj) {

    override fun areContentsTheSame(other: HexColor): Boolean {
        return this.item.color == other.color
    }

    override fun compareTo(other: HexColor): Int {
        return this.item.color.compareTo(other.color)
    }

    override fun areItemsTheSame(other: HexColor): Boolean {
        return this.item.color == other.color
    }

    override fun notifyUpdate() {

    }

    fun getColor(): Int {
        return item.color
    }

    fun getColorHex(): String {
        return item.getHex()
    }

}