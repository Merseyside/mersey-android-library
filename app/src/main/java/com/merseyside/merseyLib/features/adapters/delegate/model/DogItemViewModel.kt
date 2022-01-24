package com.merseyside.merseyLib.features.adapters.delegate.model

import com.merseyside.merseyLib.features.adapters.delegate.entity.Dog

class DogItemViewModel(obj: Dog): AnimalItemViewModel<Dog>(obj) {

    override fun notifyUpdate() {}

    override fun areContentsTheSame(other: Dog): Boolean {
        return this.item == other
    }

    override fun areItemsTheSame(other: Dog): Boolean {
        return item.name == other.name
    }
}