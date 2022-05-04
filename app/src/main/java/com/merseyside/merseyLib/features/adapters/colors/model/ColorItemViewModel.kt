package com.merseyside.merseyLib.features.adapters.colors.model

import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor

class ColorItemViewModel(obj: HexColor) : ComparableAdapterViewModel<HexColor>(obj) {

    override fun compareTo(other: HexColor): Int {
        return this.item.color.compareTo(other.color)
    }

    fun getColor(): Int {
        return item.color
    }

    fun getColorHex(): String {
        return item.getHex()
    }

}