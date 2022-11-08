package com.merseyside.merseyLib.features.adapters.contacts.model

import com.merseyside.adapters.feature.expanding.ExpandState
import com.merseyside.adapters.feature.expanding.ExpandableItem
import com.merseyside.adapters.model.NestedAdapterViewModel
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.kotlin.logger.ILogger

class ContactGroupItemViewModel(
    item: ContactGroup, override val expandState: ExpandState = ExpandState(expanded = true)
): NestedAdapterViewModel<ContactGroup, String>(item), ExpandableItem, ILogger {

    override fun areItemsTheSame(other: ContactGroup): Boolean {
        return item.group == other.group
    }

    override fun getNestedData(): List<String> {
        return item.contacts
    }

    fun getGroup(): String = item.group.toString()

    override val tag: String = "ContactGroupItemViewModel"
}