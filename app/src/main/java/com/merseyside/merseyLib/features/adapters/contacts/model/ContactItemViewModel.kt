package com.merseyside.merseyLib.features.adapters.contacts.model

import com.merseyside.adapters.model.AdapterViewModel

class ContactItemViewModel(item: String): AdapterViewModel<String>(item) {
    override fun areItemsTheSame(other: String): Boolean {
        return item == other
    }
}