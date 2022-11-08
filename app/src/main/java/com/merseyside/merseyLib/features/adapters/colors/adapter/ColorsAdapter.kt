package com.merseyside.merseyLib.features.adapters.colors.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.config
import com.merseyside.adapters.extensions.onClick
import com.merseyside.adapters.single.SimpleAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.features.adapters.colors.model.ColorItemViewModel

class ColorsAdapter(
    config: AdapterConfig<HexColor, ColorItemViewModel>
) : SimpleAdapter<HexColor, ColorItemViewModel>(config) {

    init {
        onClick {
            removeAsync(it)
        }
    }

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_color
    override fun getBindingVariable() = BR.viewModel

    override fun createItemViewModel(item: HexColor) = ColorItemViewModel(item)

    companion object {
        operator fun invoke(configure: AdapterConfig<HexColor, ColorItemViewModel>.() -> Unit): ColorsAdapter {
            return ColorsAdapter(config(configure))
        }
    }
}