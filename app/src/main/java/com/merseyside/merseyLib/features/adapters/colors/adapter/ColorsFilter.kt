package com.merseyside.merseyLib.features.adapters.colors.adapter

import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.features.adapters.colors.model.ColorItemViewModel

class ColorsFilter : FilterFeature<HexColor, ColorItemViewModel>() {

    override fun filter(model: ColorItemViewModel, key: String, filter: Any): Boolean {
        val query = filter as String
        return when (key) {
            R_COLOR_FILTER -> model.item.getRHexColor().startsWith(query, ignoreCase = true)
            G_COLOR_FILTER -> model.item.getGHexColor().startsWith(query, ignoreCase = true)
            B_COLOR_FILTER -> model.item.getBHexColor().startsWith(query, ignoreCase = true)
            else -> false
        }
    }

    companion object {
        const val R_COLOR_FILTER = "rcolor"
        const val G_COLOR_FILTER = "gcolor"
        const val B_COLOR_FILTER = "bcolor"
    }
}