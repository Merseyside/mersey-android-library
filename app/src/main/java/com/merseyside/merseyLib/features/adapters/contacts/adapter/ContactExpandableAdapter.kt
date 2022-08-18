package com.merseyside.merseyLib.features.adapters.contacts.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.NestedAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.databinding.ItemGroupContactBinding
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactGroupItemViewModel

class ContactExpandableAdapter: NestedAdapter<ContactGroup, ContactGroupItemViewModel,
        String, ContactAdapter>() {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_group_contact
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: ContactGroup) = ContactGroupItemViewModel(item)

    override fun initNestedAdapter(model: ContactGroupItemViewModel) = ContactAdapter()

    override fun getNestedView(binding: ViewDataBinding): RecyclerView {
        return (binding as ItemGroupContactBinding).recycler
    }
}