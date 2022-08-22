package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.feature.filter.interfaces.NestedFilterFeature
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactGroupItemViewModel
import com.merseyside.merseyLib.kotlin.extensions.isNotZero

class ContactsInnerFilter : NestedFilterFeature<ContactGroup, ContactGroupItemViewModel>() {

    override fun filter(model: ContactGroupItemViewModel, key: String, filter: Any): Boolean {
        return true
    }

    override fun filter(model: ContactGroupItemViewModel, innerAdapterItemsCount: Int): Boolean {
        return innerAdapterItemsCount.isNotZero()
    }

    companion object {
        const val QUERY_KEY = "query"
    }
}