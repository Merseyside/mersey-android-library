package com.merseyside.merseyLib.features.adapters.colors.model

import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor

class ColorItemViewModel(obj: HexColor) : ComparableAdapterViewModel<HexColor>(obj) {

    fun getColor(): Int {
        return item.color
    }

    fun getColorHex(): String {
        return item.getHex()
    }

    override fun compareTo(other: HexColor): Int {
        return getColor().compareTo(other.color)
    }

    override fun toString(): String {
        return getColorHex()
    }

}