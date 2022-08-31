package com.merseyside.merseyLib.features.adapters.delegate.adapter

import com.merseyside.adapters.delegates.composites.SortedCompositeAdapter
import com.merseyside.adapters.extensions.onItemClicked
import com.merseyside.merseyLib.features.adapters.delegate.entity.Animal
import com.merseyside.merseyLib.features.adapters.delegate.model.AnimalItemViewModel
import com.merseyside.merseyLib.kotlin.logger.log

class AnimalsAdapter: SortedCompositeAdapter<Animal, AnimalItemViewModel<Animal>>() {

    init {
        delegatesManager.addDelegates(
            CatDelegateAdapter(),
            DogDelegateAdapter()
        )
    }
}