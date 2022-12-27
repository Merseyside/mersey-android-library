package com.merseyside.merseyLib.features.adapters.racers.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.config
import com.merseyside.adapters.holder.TypedBindingHolder
import com.merseyside.adapters.model.AdapterParentViewModel
import com.merseyside.adapters.single.SimpleAdapter
import com.merseyside.animators.template.SetTextFadeOutInAnimator
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.databinding.ItemCheckpointBinding
import com.merseyside.merseyLib.features.adapters.racers.entity.Checkpoint
import com.merseyside.merseyLib.features.adapters.racers.model.CheckpointItemViewModel
import com.merseyside.merseyLib.time.units.Millis
import com.merseyside.merseyLib.time.units.compareTo
import com.merseyside.utils.view.ext.setTextColorAttr

class RacersAdapter(config: AdapterConfig<Checkpoint, CheckpointItemViewModel>) :
    SimpleAdapter<Checkpoint, CheckpointItemViewModel>(config) {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_checkpoint
    override fun getBindingVariable() = BR.viewModel
    override fun createItemViewModel(item: Checkpoint) = CheckpointItemViewModel(item)

    override fun onPayloadable(
        holder: TypedBindingHolder<CheckpointItemViewModel>,
        payloads: List<AdapterParentViewModel.Payloadable>
    ) {
        payloads.forEach {
            when (it) {
                is CheckpointItemViewModel.CheckpointPayloads.ChangeGap -> {
                    SetTextFadeOutInAnimator(
                        view = (holder.binding as ItemCheckpointBinding).gapChange,
                        text = getGapChangeFormatted(it.gapChange),
                        duration = Millis(500),
                        onInvisibleState = { view -> view.setTextColorAttr(getGapChangeColor(it.gapChange)) }
                    ).start()
                }
                else -> {
                }
            }
        }
    }

    private fun getGapChangeFormatted(gapChange: Millis): String {
        return if (gapChange.isNotEmpty()) {
            if (gapChange > 0) {
                "+ ${gapChange.millis} ms"
            } else {
                "${gapChange.millis} ms"
            }
        } else {
            ""
        }
    }

    private fun getGapChangeColor(gapChange: Millis): Int {
        return if (gapChange < 0) {
            R.attr.positive_text_color
        } else {
            R.attr.negative_text_color
        }
    }

    companion object {
        operator fun invoke(configure: AdapterConfig<Checkpoint, CheckpointItemViewModel>.() -> Unit): RacersAdapter {
            return RacersAdapter(config(configure))
        }
    }
}