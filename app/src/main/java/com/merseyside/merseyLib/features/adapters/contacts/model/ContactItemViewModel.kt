package com.merseyside.merseyLib.features.adapters.contacts.model

import com.merseyside.adapters.model.ComparableAdapterViewModel

class ContactItemViewModel(obj: String): ComparableAdapterViewModel<String>(obj) {
    override fun areItemsTheSame(other: String): Boolean {
        return this.item == other
    }

    override fun notifyUpdate() {}

    override fun areContentsTheSame(other: String): Boolean {
        return areParentItemsTheSame(other)
    }

    override fun compareTo(other: String): Int {
        return this.item.compareTo(other)
    }
}