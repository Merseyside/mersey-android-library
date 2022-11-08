package com.merseyside.merseyLib.features.adapters.racers.model

import androidx.annotation.AttrRes
import androidx.databinding.Bindable
import com.merseyside.adapters.model.AdapterViewModel
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.racers.entity.Checkpoint
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.time.ext.toFormattedDate
import com.merseyside.merseyLib.time.units.Millis
import com.merseyside.merseyLib.time.units.compareTo
import com.merseyside.merseyLib.time.units.minus

class CheckpointItemViewModel(item: Checkpoint) : AdapterViewModel<Checkpoint>(item) {

    private var gapChange: Millis = Millis()
    private var rank: Int = 0

    override fun notifyUpdate() {
        notifyPropertyChanged(BR.gap)
    }

    fun getRacer(): String {
        return item.racer.name
    }

    fun getTeam(): String {
        return item.team
    }

    fun getImage(): String {
        return item.racer.image
    }

    override fun onPositionChanged(fromPosition: Int, toPosition: Int) {
        this.rank = toPosition
        notifyPropertyChanged(BR.rank)
    }

    @Bindable
    fun getRank(): String {
        return "${position + 1}."
    }

    @Bindable
    fun getGap(): String {
        return item.gap.toFormattedDate("ss:SSS").date
    }

    fun getChangeGap(): String {
        gapChange.log(prefix = "gap change")
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

    @AttrRes
    fun getGapChangeColor(): Int {
        return if (gapChange < 0) {
            R.attr.positive_text_color
        } else {
            R.attr.negative_text_color
        }
    }

    override fun payload(oldItem: Checkpoint, newItem: Checkpoint): List<Payloadable> {
        gapChange = if (newItem.gap.isEmpty()) {
            newItem.gap
        } else {
            newItem.gap - oldItem.gap
        }

        return listOf(CheckpointPayloads.ChangeGap(gapChange = gapChange))
    }

    sealed class CheckpointPayloads : Payloadable {
        class ChangeGap(
            val gapChange: Millis
        ) : CheckpointPayloads()
    }
}