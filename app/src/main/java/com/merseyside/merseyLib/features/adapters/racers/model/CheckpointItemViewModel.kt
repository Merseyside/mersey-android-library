package com.merseyside.merseyLib.features.adapters.racers.model

import androidx.annotation.AttrRes
import androidx.databinding.Bindable
import com.merseyside.adapters.model.ComparableAdapterViewModel
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.racers.entity.Checkpoint
import com.merseyside.merseyLib.time.Millis
import com.merseyside.merseyLib.time.ext.toFormattedDate
import com.merseyside.utils.time.compareTo
import com.merseyside.utils.time.minus

class CheckpointItemViewModel(obj: Checkpoint) : ComparableAdapterViewModel<Checkpoint>(obj) {

    private var gapChange: Millis = Millis()
    private var rank: Int = 0

    override fun areContentsTheSame(obj: Checkpoint): Boolean {
        return this.obj == obj
    }

    override fun compareTo(obj: Checkpoint): Int {
        return this.obj.gap.compareTo(obj.gap)
    }

    override fun areItemsTheSame(obj: Checkpoint): Boolean {
        return this.obj.racer == obj.racer
    }

    override fun notifyUpdate() {
        notifyPropertyChanged(BR.gap)
        notifyPropertyChanged(BR.rank)
    }

    fun getRacer(): String {
        return obj.racer.name
    }

    fun getTeam(): String {
        return obj.team
    }

    fun getImage(): String {
        return obj.racer.image
    }

    override fun onPositionChanged(fromPosition: Int, toPosition: Int) {
        this.rank = toPosition
        notifyPropertyChanged(BR.rank)
    }

    @Bindable
    fun getRank(): String {
        return "${getPosition() + 1}."
    }

    @Bindable
    fun getGap(): String {
        return obj.gap.toFormattedDate("ss:SSS").value
    }

    fun getChangeGap(): String {
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

    override fun payload(newItem: Checkpoint): List<Payloadable> {
        gapChange = if (newItem.gap.isEmpty()) {
            Millis(0)
        } else {
            newItem.gap - obj.gap
        }

        super.payload(newItem)

        return listOf(
            CheckpointPayloads.ChangeGap(gapChange = gapChange)
        )
    }

    sealed class CheckpointPayloads : Payloadable {
        class ChangeGap(
            val gapChange: Millis
        ) : CheckpointPayloads()
    }
}