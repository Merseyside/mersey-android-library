package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.feature.filter.AdapterFilter
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactItemViewModel

class ContactsFilter : AdapterFilter<String, ContactItemViewModel>() {

    override fun filter(model: ContactItemViewModel, key: String, filter: Any): Boolean {
        filter as String
        return model.item.contains(filter, ignoreCase = true)
    }
}