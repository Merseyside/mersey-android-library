package com.merseyside.merseyLib.features.adapters.adapter

import com.merseyside.adapters.base.BaseSortedAdapter
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.features.adapters.entity.HexColor
import com.merseyside.merseyLib.features.adapters.model.ColorItemViewModel
import com.merseyside.utils.ext.forEachIsTrue
import com.merseyside.utils.ext.log

class SampleAdapter : BaseSortedAdapter<HexColor, ColorItemViewModel>() {

    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.item_color
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun createItemViewModel(obj: HexColor): ColorItemViewModel {
        return ColorItemViewModel(obj)
    }

    override fun filter(obj: ColorItemViewModel, key: String, filterObj: Any): Boolean {
        val query = filterObj as String
        return when (key) {
            R_COLOR_FILTER -> obj.getItem().getRHexColor().startsWith(query, ignoreCase = true)
            G_COLOR_FILTER -> obj.getItem().getGHexColor().startsWith(query, ignoreCase = true)
            B_COLOR_FILTER -> obj.getItem().getBHexColor().startsWith(query, ignoreCase = true)
            else -> false
        }
    }

    companion object {
        const val R_COLOR_FILTER = "rcolor"
        const val G_COLOR_FILTER = "gcolor"
        const val B_COLOR_FILTER = "bcolor"
    }
}