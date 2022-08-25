package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.feature.filter.NestedFilterFeature
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactGroupItemViewModel

class ContactsInnerFilter : NestedFilterFeature<ContactGroup, ContactGroupItemViewModel>() {

    override fun filter(model: ContactGroupItemViewModel, key: String, filter: Any): Boolean {
        return true
    }

    override fun filter(model: ContactGroupItemViewModel, hasItems: Boolean): Boolean {
        return hasItems.log("kek", "has items")
    }

    companion object {
        const val QUERY_KEY = "query"
    }
}