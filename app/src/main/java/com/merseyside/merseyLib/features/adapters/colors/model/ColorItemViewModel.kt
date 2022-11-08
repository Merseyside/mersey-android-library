package com.merseyside.merseyLib.features.adapters.colors.model

import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor

class ColorItemViewModel(obj: HexColor) : AdapterViewModel<HexColor>(obj) {

    fun getColor(): Int {
        return item.color
    }

    fun getColorHex(): String {
        return item.getHex()
    }

    override fun toString(): String {
        return getColorHex()
    }

}