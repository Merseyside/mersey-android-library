package com.merseyside.merseyLib.features.adapters.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.merseyside.adapters.base.UpdateRequest
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentAdaptersBinding
import com.merseyside.merseyLib.features.adapters.adapter.SampleAdapter
import com.merseyside.merseyLib.features.adapters.di.AdaptersModule
import com.merseyside.merseyLib.features.adapters.di.DaggerAdaptersComponent
import com.merseyside.merseyLib.features.adapters.model.AdaptersViewModel
import com.merseyside.utils.ext.addTextChangeListener
import com.merseyside.utils.ext.isZero
import com.merseyside.utils.ext.log

class AdaptersFragment : BaseSampleFragment<FragmentAdaptersBinding, AdaptersViewModel>() {

    private val adapter = SampleAdapter(lifecycleScope)

    override fun getBindingVariable() = BR.viewModel

    override fun performInjection(bundle: Bundle?) {
        DaggerAdaptersComponent.builder()
            .appComponent(appComponent)
            .adaptersModule(AdaptersModule(this))
            .build().inject(this)
    }

    override fun getLayoutId() = R.layout.fragment_adapters
    override fun getTitle(context: Context) = "Adapters"
    override fun hasTitleBackButton() = true

    private val textChangeListener = {
            view: View,
            newValue: String?,
            oldValue: String?,
            length: Int,
            _: Int,
            _: Int,
            _: Int ->

        val filterName = when (view.id) {
            binding.rColor.id -> SampleAdapter.R_COLOR_FILTER
            binding.gColor.id -> SampleAdapter.G_COLOR_FILTER
            binding.bColor.id -> SampleAdapter.B_COLOR_FILTER
            else -> throw IllegalArgumentException()
        }

        if (length in 1..2) {
            adapter.addFilter(filterName, newValue!!)
            true
        } else {
            if (length.isZero()) {
                adapter.removeFilter(filterName)
                true
            } else {
                false
            }
        }.also { adapter.applyFilters() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        binding.rColor.addTextChangeListener(textChangeListener)
        binding.gColor.addTextChangeListener(textChangeListener)
        binding.bColor.addTextChangeListener(textChangeListener)

        viewModel.getColorsFlow().asLiveData().observe(viewLifecycleOwner) {
            if (binding.add.isChecked) {

                if (binding.async.isChecked) {
                    adapter.addAsync(it)
                } else {
                    adapter.add(it)
                }
            } else {
                val updateRequest = UpdateRequest.Builder(it)
                    .isAddNew(binding.updateAdd.isChecked)
                    .isDeleteOld(binding.updateRemove.isChecked)
                    .build()

                if (binding.async.isChecked) {
                    adapter.updateAsync(updateRequest)
                } else {
                    adapter.update(updateRequest)
                }
            }
        }
    }

}