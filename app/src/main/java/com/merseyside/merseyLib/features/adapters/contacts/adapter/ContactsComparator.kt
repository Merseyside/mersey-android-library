package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactGroupItemViewModel

object ContactsComparator : Comparator<ContactGroup, ContactGroupItemViewModel>() {
    override fun compare(
        model1: ContactGroupItemViewModel,
        model2: ContactGroupItemViewModel
    ): Int {
        return model1.item.group.compareTo(model2.item.group)
    }
}