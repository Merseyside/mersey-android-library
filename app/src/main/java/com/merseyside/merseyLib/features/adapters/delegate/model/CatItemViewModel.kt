package com.merseyside.merseyLib.features.adapters.delegate.model

import com.merseyside.merseyLib.features.adapters.delegate.entity.Cat

class CatItemViewModel(obj: Cat): AnimalItemViewModel<Cat>(obj) {

    override fun notifyUpdate() {}

    override fun areContentsTheSame(other: Cat): Boolean {
        return areParentContentsTheSame(other)
    }

    override fun areItemsTheSame(other: Cat): Boolean {
        return areParentItemsTheSame(other)
    }
}