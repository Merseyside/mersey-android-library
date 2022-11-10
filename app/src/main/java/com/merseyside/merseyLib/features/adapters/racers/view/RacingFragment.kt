package com.merseyside.merseyLib.features.adapters.racers.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.adapters.feature.sorting.Sorting
import com.merseyside.adapters.decorator.SimpleItemOffsetDecorator
import com.merseyside.adapters.extensions.Behaviour
import com.merseyside.adapters.extensions.setFlow
import com.merseyside.adapters.feature.positioning.Positioning
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentRacingBinding
import com.merseyside.merseyLib.features.adapters.racers.adapter.RacersAdapter
import com.merseyside.merseyLib.features.adapters.racers.adapter.RacersComparator
import com.merseyside.merseyLib.features.adapters.racers.di.DaggerRacingComponent
import com.merseyside.merseyLib.features.adapters.racers.di.RacingModule
import com.merseyside.merseyLib.features.adapters.racers.model.RacingViewModel

class RacingFragment : BaseSampleFragment<FragmentRacingBinding, RacingViewModel>() {

    private val adapter = initAdapter(::RacersAdapter) {
        Sorting {
            comparator = RacersComparator
        }

        Positioning()
    }

    override fun hasTitleBackButton() = true
    override fun getBindingVariable() = BR.viewModel
    override fun getLayoutId() = R.layout.fragment_racing
    override fun getTitle(context: Context) = "Racing"

    override fun performInjection(bundle: Bundle?, vararg params: Any) {
        DaggerRacingComponent.builder()
            .appComponent(appComponent)
            .racingModule(RacingModule(this))
            .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireBinding().racersList.apply {
            adapter = this@RacingFragment.adapter
            addItemDecoration(
                SimpleItemOffsetDecorator(
                    context,
                    R.dimen.small_spacing,
                    R.dimen.normal_spacing
                )
            )
        }

        adapter.setFlow(
            flow = viewModel.getCheckpointFlow(),
            viewLifecycleOwner = viewLifecycleOwner,
            behaviour = Behaviour.ADD_UPDATE(removeOld = false)
        )
    }
}