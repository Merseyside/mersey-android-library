package com.merseyside.merseyLib.features.adapters.racers.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.merseyside.adapters.base.UpdateRequest
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentRacingBinding
import com.merseyside.merseyLib.features.adapters.racers.adapter.RacersAdapter
import com.merseyside.merseyLib.features.adapters.racers.di.DaggerRacingComponent
import com.merseyside.merseyLib.features.adapters.racers.di.RacingModule
import com.merseyside.merseyLib.features.adapters.racers.model.RacingViewModel

class RacingFragment : BaseSampleFragment<FragmentRacingBinding, RacingViewModel>() {

    private val adapter = RacersAdapter(lifecycleScope)

    override fun hasTitleBackButton() = true
    override fun getBindingVariable() = BR.viewModel
    override fun getLayoutId() = R.layout.fragment_racing
    override fun getTitle(context: Context) = "Racing"

    override fun performInjection(bundle: Bundle?) {
        DaggerRacingComponent.builder()
            .appComponent(appComponent)
            .racingModule(RacingModule(this))
            .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireBinding().racersList.apply {
            adapter = this@RacingFragment.adapter
            addItemDecoration(CheckpointItemDecorator(context, R.dimen.small_spacing))
        }


        viewModel.getCheckpointFlow().asLiveData().observe(viewLifecycleOwner) {
            adapter.updateAsync(UpdateRequest(it))
        }
    }
}