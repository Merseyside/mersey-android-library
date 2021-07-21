package com.merseyside.merseyLib.features.adapters.colors.model

import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor

class ColorItemViewModel(obj: HexColor) : ComparableAdapterViewModel<HexColor>(obj) {

    override fun areContentsTheSame(obj: HexColor): Boolean {
        return this.obj.color == obj.color
    }

    override fun compareTo(obj: HexColor): Int {
        return when {
            obj.color < this.obj.color -> {
                1
            }
            obj.color > this.obj.color -> {
                -1
            }
            else -> {
                0
            }
        }
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