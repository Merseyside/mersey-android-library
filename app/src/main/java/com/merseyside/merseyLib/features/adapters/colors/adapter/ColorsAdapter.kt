package com.merseyside.merseyLib.features.adapters.colors.adapter

import com.merseyside.adapters.base.SimpleAdapter
import com.merseyside.adapters.ext.onItemClicked
import com.merseyside.adapters.feature.filter.Filterable
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.features.adapters.colors.model.ColorItemViewModel
import kotlinx.coroutines.CoroutineScope

class ColorsAdapter(scope: CoroutineScope) : SimpleAdapter<HexColor, ColorItemViewModel>(scope),
    Filterable<HexColor, ColorItemViewModel> {

    override val filter = ColorsFilter()

    init {
        onItemClicked {
            remove(it)
        }
    }

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_color
    override fun getBindingVariable() = BR.viewModel
    override fun createItemViewModel(item: HexColor) = ColorItemViewModel(item)
}