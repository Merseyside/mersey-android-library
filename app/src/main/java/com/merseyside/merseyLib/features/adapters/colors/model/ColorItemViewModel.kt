package com.merseyside.merseyLib.features.adapters.colors.model

import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.kotlin.contract.Identifiable

class ColorItemViewModel(obj: HexColor) : ComparableAdapterViewModel<HexColor>(obj), Identifiable<Int> {

    override fun getId(): Int {
        return getColor()
    }

    fun getColor(): Int {
        return item.color
    }

    fun getColorHex(): String {
        return item.getHex()
    }

}