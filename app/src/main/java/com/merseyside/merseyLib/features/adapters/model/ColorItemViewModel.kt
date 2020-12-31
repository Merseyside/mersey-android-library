package com.merseyside.merseyLib.features.adapters.model

import com.merseyside.adapters.model.BaseComparableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.entity.HexColor

class ColorItemViewModel(obj: HexColor) : BaseComparableAdapterViewModel<HexColor>(obj) {

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