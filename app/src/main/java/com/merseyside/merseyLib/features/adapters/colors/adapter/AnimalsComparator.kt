package com.merseyside.merseyLib.features.adapters.colors.adapter

import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.merseyLib.features.adapters.delegate.entity.Animal
import com.merseyside.merseyLib.features.adapters.delegate.model.AnimalItemViewModel

class AnimalsComparator : Comparator<Animal, AnimalItemViewModel<out Animal>>() {
    override fun compare(
        model1: AnimalItemViewModel<out Animal>,
        model2: AnimalItemViewModel<out Animal>
    ): Int {
        return model1.getAge().compareTo(model2.getAge())
    }
}