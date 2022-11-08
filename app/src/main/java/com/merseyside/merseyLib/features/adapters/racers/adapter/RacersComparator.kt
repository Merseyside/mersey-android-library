package com.merseyside.merseyLib.features.adapters.racers.adapter

import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.merseyLib.features.adapters.racers.entity.Checkpoint
import com.merseyside.merseyLib.features.adapters.racers.model.CheckpointItemViewModel

object RacersComparator : Comparator<Checkpoint, CheckpointItemViewModel>() {
    override fun compare(
        model1: CheckpointItemViewModel,
        model2: CheckpointItemViewModel
    ): Int {
        return model1.item.gap.compareTo(model2.item.gap)
    }
}