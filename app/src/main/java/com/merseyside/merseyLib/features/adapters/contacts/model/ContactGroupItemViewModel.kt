package com.merseyside.merseyLib.features.adapters.contacts.model

import com.merseyside.adapters.model.NestedAdapterViewModel
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup

class ContactGroupItemViewModel(
    item: ContactGroup
): NestedAdapterViewModel<ContactGroup, String>(item) {
    override fun areItemsTheSame(other: ContactGroup): Boolean {
        return item.group == other.group
    }

    override fun compareTo(other: ContactGroup): Int {
        return item.group.compareTo(other.group)
    }

    override fun getNestedData(): List<String> {
        return item.contacts
    }

    fun getGroup(): String = item.group.toString()
}