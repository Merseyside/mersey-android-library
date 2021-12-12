package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.base.SortedAdapter
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactItemViewModel

class ContactAdapter: SortedAdapter<String, ContactItemViewModel>() {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_contact
    override fun getBindingVariable() = BR.obj
    override fun createItemViewModel(obj: String) = ContactItemViewModel(obj)
}