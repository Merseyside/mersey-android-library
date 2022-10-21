package com.merseyside.merseyLib.features.adapters.colors.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.merseyside.adapters.feature.filter.Filtering
import com.merseyside.adapters.feature.sorting.Sorting
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.archy.presentation.view.valueSwitcher.ValueSwitcher
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentColorsBinding
import com.merseyside.merseyLib.features.adapters.colors.adapter.ColorsAdapter
import com.merseyside.merseyLib.features.adapters.colors.adapter.ColorsComparator
import com.merseyside.merseyLib.features.adapters.colors.adapter.ColorsFilter
import com.merseyside.merseyLib.features.adapters.colors.di.ColorsModule
import com.merseyside.merseyLib.features.adapters.colors.di.DaggerColorsComponent
import com.merseyside.merseyLib.features.adapters.colors.model.ColorsViewModel
import com.merseyside.merseyLib.kotlin.extensions.isZero
import com.merseyside.utils.view.ext.addTextChangeListener

class ColorsFragment : BaseSampleFragment<FragmentColorsBinding, ColorsViewModel>() {

    private val colorsFilter = ColorsFilter()
    private val colorsComparator = ColorsComparator(ColorsComparator.ColorComparisonRule.ASC)

    private val adapter = ColorsAdapter {
        coroutineScope = lifecycleScope

        Sorting {
            comparator = colorsComparator
        }

        Filtering {
            filter = colorsFilter
        }
    }

    override fun getBindingVariable() = BR.viewModel
    override fun getLayoutId() = R.layout.fragment_colors
    override fun getTitle(context: Context) = "Colors"
    override fun hasTitleBackButton() = true

    override fun performInjection(bundle: Bundle?, vararg params: Any) {
        DaggerColorsComponent.builder()
            .appComponent(appComponent)
            .colorsModule(ColorsModule(this))
            .build().inject(this)
    }

    private val textChangeListener = { view: View,
                                       newValue: String?,
                                       _: String?,
                                       length: Int,
                                       _: Int,
                                       _: Int,
                                       _: Int ->

        if (newValue != null) {
            val filterName = when (view.id) {
                requireBinding().rColor.id -> ColorsFilter.R_COLOR_FILTER
                requireBinding().gColor.id -> ColorsFilter.G_COLOR_FILTER
                requireBinding().bColor.id -> ColorsFilter.B_COLOR_FILTER
                else -> throw IllegalArgumentException()
            }

            if (length in 1..2) {
                colorsFilter.addFilter(filterName, newValue)
                true
            } else {
                if (length.isZero()) {
                    colorsFilter.removeFilter(filterName)
                    true
                } else {
                    false
                }
            }.also {
                colorsFilter.applyFiltersAsync()
            }
        } else false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireBinding().run {
            recyclerView.adapter = adapter
            rColor.addTextChangeListener(textChangeListener)
            gColor.addTextChangeListener(textChangeListener)
            bColor.addTextChangeListener(textChangeListener)
        }

        requireBinding().sortSwitcher.setOnValueChangeListener(
            object : ValueSwitcher.OnValueChangeListener {
                override fun valueChanged(entryValue: String) {
                    colorsComparator.setCompareRule(
                        ColorsComparator.ColorComparisonRule.valueOf(
                            entryValue.uppercase()
                        )
                    )
                }
            })


        viewModel.getColorsFlow().asLiveData().observe(viewLifecycleOwner) {
            if (requireBinding().add.isChecked) {
                adapter.addAsync(it)
            } else {
                val updateRequest = UpdateRequest.Builder(it)
                    .isAddNew(requireBinding().updateAdd.isChecked)
                    .isDeleteOld(requireBinding().updateRemove.isChecked)
                    .build()

                adapter.updateAsync(updateRequest)
            }
        }
    }

}