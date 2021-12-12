package com.merseyside.merseyLib.features.adapters.contacts.model

import com.merseyside.adapters.model.ExpandableAdapterViewModel
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup

class ContactGroupItemViewModel(
    obj: ContactGroup
): ExpandableAdapterViewModel<ContactGroup, String>(obj) {
    override fun areItemsTheSame(obj: ContactGroup): Boolean {
        return this.obj.group == obj.group
    }

    override fun notifyUpdate() {}

    override fun areContentsTheSame(obj: ContactGroup): Boolean {
        return this.obj == obj
    }

    override fun compareTo(obj: ContactGroup): Int {
        return this.obj.group.compareTo(obj.group)
    }

    override fun getExpandedData(): List<String> {
        return obj.contacts
    }

    override fun onExpanded(isExpanded: Boolean) {}
    override fun notifySelectEnabled(isEnabled: Boolean) {}

    fun getGroup(): String = obj.group.toString()
}