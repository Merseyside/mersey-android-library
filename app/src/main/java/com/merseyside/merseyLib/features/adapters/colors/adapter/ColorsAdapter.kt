package com.merseyside.merseyLib.features.adapters.colors.adapter

import com.merseyside.adapters.base.SortedAdapter
import com.merseyside.adapters.ext.onItemClicked
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.features.adapters.colors.model.ColorItemViewModel
import kotlinx.coroutines.CoroutineScope

class ColorsAdapter(scope: CoroutineScope) : SortedAdapter<HexColor, ColorItemViewModel>(scope) {

    init {
        onItemClicked {
            remove(it)
        }
    }

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_color
    override fun getBindingVariable() = BR.viewModel
    override fun createItemViewModel(item: HexColor) = ColorItemViewModel(item)

    override fun filter(model: ColorItemViewModel, key: String, filterObj: Any): Boolean {
        val query = filterObj as String
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