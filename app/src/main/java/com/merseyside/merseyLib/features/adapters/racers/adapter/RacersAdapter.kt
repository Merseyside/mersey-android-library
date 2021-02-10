package com.merseyside.merseyLib.features.adapters.racers.adapter

import com.merseyside.adapters.base.BaseSortedAdapter
import com.merseyside.adapters.model.BaseComparableAdapterViewModel
import com.merseyside.adapters.view.TypedBindingHolder
import com.merseyside.animators.template.SetTextFadeOutInAnimator
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.databinding.ItemCheckpointBinding
import com.merseyside.merseyLib.features.adapters.racers.entity.Checkpoint
import com.merseyside.merseyLib.features.adapters.racers.model.CheckpointItemViewModel
import com.merseyside.utils.ext.setTextColorAttr
import com.merseyside.utils.time.Millis
import com.merseyside.utils.time.compareTo
import kotlinx.coroutines.CoroutineScope

class RacersAdapter(scope: CoroutineScope) :
    BaseSortedAdapter<Checkpoint, CheckpointItemViewModel>(scope) {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_checkpoint
    override fun getBindingVariable() = BR.viewModel
    override fun createItemViewModel(obj: Checkpoint) = CheckpointItemViewModel(obj)

    override fun onPayloadable(
        holder: TypedBindingHolder<CheckpointItemViewModel>,
        payloads: List<BaseComparableAdapterViewModel.Payloadable>
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
                "+ ${gapChange.toMillisLong()} ms"
            } else {
                "${gapChange.toMillisLong()} ms"
            }
        } else {
            ""
        }
    }

    fun getGapChangeColor(gapChange: Millis): Int {
        return if (gapChange < 0) {
            R.attr.positive_text_color
        } else {
            R.attr.negative_text_color
        }
    }
}