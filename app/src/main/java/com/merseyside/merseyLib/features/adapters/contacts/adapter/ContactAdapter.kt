package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.feature.filter.interfaces.FilterFeature
import com.merseyside.adapters.feature.filter.interfaces.Filterable
import com.merseyside.adapters.single.SortedAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactItemViewModel

class ContactAdapter: SortedAdapter<String, ContactItemViewModel>(), Filterable<String, ContactItemViewModel> {

    override val filter: FilterFeature<String, ContactItemViewModel> = ContactsFilter()

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_contact
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: String) = ContactItemViewModel(item)

}