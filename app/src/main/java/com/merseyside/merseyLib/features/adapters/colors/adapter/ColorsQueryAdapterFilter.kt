package com.merseyside.merseyLib.features.adapters.colors.adapter

import com.merseyside.adapters.feature.filtering.QueryAdapterFilter
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.features.adapters.colors.model.ColorItemViewModel

class ColorsQueryAdapterFilter : QueryAdapterFilter<HexColor, ColorItemViewModel>() {
    override fun filter(model: ColorItemViewModel, query: String): Boolean {
        return model.item.getRHexColor().startsWith(query, ignoreCase = true)
    }
}