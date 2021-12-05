package com.merseyside.merseyLib.features.adapters.colors.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.merseyside.adapters.base.UpdateRequest
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentColorsBinding
import com.merseyside.merseyLib.features.adapters.colors.adapter.ColorsAdapter
import com.merseyside.merseyLib.features.adapters.colors.di.ColorsModule
import com.merseyside.merseyLib.features.adapters.colors.di.DaggerColorsComponent
import com.merseyside.merseyLib.features.adapters.colors.model.ColorsViewModel
import com.merseyside.merseyLib.kotlin.extensions.isZero
import com.merseyside.utils.ext.addTextChangeListener

class ColorsFragment : BaseSampleFragment<FragmentColorsBinding, ColorsViewModel>() {

    private val adapter = ColorsAdapter(lifecycleScope)

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

    private val textChangeListener = {
            view: View,
            newValue: String?,
            _: String?,
            length: Int,
            _: Int,
            _: Int,
            _: Int ->

        if (newValue != null) {
            val filterName = when (view.id) {
                requireBinding().rColor.id -> ColorsAdapter.R_COLOR_FILTER
                requireBinding().gColor.id -> ColorsAdapter.G_COLOR_FILTER
                requireBinding().bColor.id -> ColorsAdapter.B_COLOR_FILTER
                else -> throw IllegalArgumentException()
            }

            if (length in 1..2) {
                adapter.addFilter(filterName, newValue)
                true
            } else {
                if (length.isZero()) {
                    adapter.removeFilter(filterName)
                    true
                } else {
                    false
                }
            }.also { adapter.applyFilters() }
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

        viewModel.getColorsFlow().asLiveData().observe(viewLifecycleOwner) {
            if (requireBinding().add.isChecked) {

                if (requireBinding().async.isChecked) {
                    adapter.addAsync(it)
                } else {
                    adapter.add(it)
                }
            } else {
                val updateRequest = UpdateRequest.Builder(it)
                    .isAddNew(requireBinding().updateAdd.isChecked)
                    .isDeleteOld(requireBinding().updateRemove.isChecked)
                    .build()

                if (requireBinding().async.isChecked) {
                    adapter.updateAsync(updateRequest)
                } else {
                    adapter.update(updateRequest)
                }
            }
        }
    }

}