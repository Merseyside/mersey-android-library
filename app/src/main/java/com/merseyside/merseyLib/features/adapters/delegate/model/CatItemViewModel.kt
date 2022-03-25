package com.merseyside.merseyLib.features.adapters.delegate.model

import com.merseyside.merseyLib.features.adapters.delegate.entity.Cat

class CatItemViewModel(item: Cat): AnimalItemViewModel<Cat>(item) {

    override fun notifyUpdate() {}

    override fun areContentsTheSame(other: Cat): Boolean {
        return this.item == other
    }

    override fun areItemsTheSame(other: Cat): Boolean {
        return areParentItemsTheSame(other)
    }
}