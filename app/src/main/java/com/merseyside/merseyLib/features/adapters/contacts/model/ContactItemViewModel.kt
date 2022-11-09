package com.merseyside.merseyLib.features.adapters.contacts.model

import com.merseyside.adapters.model.ComparableAdapterViewModel

class ContactItemViewModel(item: String): ComparableAdapterViewModel<String>(item) {
    override fun areItemsTheSame(other: String): Boolean {
        return item == other
    }

    override fun compareTo(other: String): Int {
        return this.item.compareTo(other)
    }
}