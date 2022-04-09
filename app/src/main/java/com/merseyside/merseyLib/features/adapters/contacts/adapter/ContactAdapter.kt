package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.base.BaseAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactItemViewModel

class ContactAdapter: BaseAdapter<String, ContactItemViewModel>() {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_contact
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: String) = ContactItemViewModel(item)
}