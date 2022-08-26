package com.merseyside.merseyLib.features.adapters.colors.adapter

import com.merseyside.adapters.extensions.onItemClicked
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.single.SortedAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.features.adapters.colors.model.ColorItemViewModel
import kotlinx.coroutines.CoroutineScope

class ColorsAdapter(scope: CoroutineScope) : SortedAdapter<HexColor, ColorItemViewModel>(scope),
    Filterable<HexColor, ColorItemViewModel> {

    override val filter = ColorsFilter()

    private val colorsComparator: ColorsComparator =
        ColorsComparator(ColorsComparator.ColorComparisonRule.ASC)

    init {
        comparator = colorsComparator
        onItemClicked {
            removeAsync(it)
        }
    }

    fun setComparisonRule(rule: ColorsComparator.ColorComparisonRule) {
        colorsComparator.setCompareRule(rule)
    }

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_color
    override fun getBindingVariable() = BR.viewModel
    override fun createItemViewModel(item: HexColor) = ColorItemViewModel(item)
}