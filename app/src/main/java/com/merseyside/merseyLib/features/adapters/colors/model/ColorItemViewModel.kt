package com.merseyside.merseyLib.features.adapters.colors.model

import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor

class ColorItemViewModel(obj: HexColor) : ComparableAdapterViewModel<HexColor>(obj) {

    override fun areContentsTheSame(obj: HexColor): Boolean {
        return this.obj.color == obj.color
    }

    override fun compareTo(obj: HexColor): Int {
        return this.obj.color.compareTo(obj.color)
    }

    override fun areItemsTheSame(obj: HexColor): Boolean {
        return this.obj.color == obj.color
    }

    override fun notifyUpdate() {

    }

    fun getColor(): Int {
        return obj.color
    }

    fun getColorHex(): String {
        return obj.getHex()
    }
}