package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.feature.filter.NestedAdapterFilter
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactGroupItemViewModel

class ContactsInnerAdapterFilter : NestedAdapterFilter<ContactGroup, ContactGroupItemViewModel>() {

    override fun filter(model: ContactGroupItemViewModel, key: String, filter: Any): Boolean {
        return true
    }

    override fun filter(model: ContactGroupItemViewModel, hasItems: Boolean): Boolean {
        return hasItems
    }

    companion object {
        const val QUERY_KEY = "query"
    }
}