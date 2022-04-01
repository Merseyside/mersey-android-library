package com.merseyside.merseyLib.features.adapters.contacts.model

import com.merseyside.adapters.model.ExpandableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup

class ContactGroupItemViewModel(
    item: ContactGroup
): ExpandableAdapterViewModel<ContactGroup, String>(item) {
    override fun areItemsTheSame(other: ContactGroup): Boolean {
        return item.group == other.group
    }

    override fun notifyUpdate() {}

    override fun areContentsTheSame(other: ContactGroup): Boolean {
        return item == other
    }

    override fun compareTo(other: ContactGroup): Int {
        return item.group.compareTo(other.group)
    }

    override fun getExpandedData(): List<String> {
        return item.contacts
    }

    override fun onExpanded(isExpanded: Boolean) {}
    override fun notifySelectEnabled(isEnabled: Boolean) {}

    fun getGroup(): String = item.group.toString()
}