package com.merseyside.merseyLib.features.adapters.contacts.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.base.ExpandableAdapter
import com.merseyside.adapters.base.UpdateRequest
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.databinding.ItemGroupContactBinding
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactGroupItemViewModel

class ContactExpandableAdapter: ExpandableAdapter<ContactGroup, ContactGroupItemViewModel,
        String, ContactAdapter>() {
    override fun getLayoutIdForPosition(position: Int) = R.layout.item_group_contact
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: ContactGroup) = ContactGroupItemViewModel(item)

    override fun initExpandableList(model: ContactGroupItemViewModel) = ContactAdapter()

    override fun getExpandableView(binding: ViewDataBinding): RecyclerView {
        return (binding as ItemGroupContactBinding).recycler
    }

    override fun getExpandableAdapterUpdateRequest(data: List<String>?): UpdateRequest<String>? {
        return data?.let { UpdateRequest(it) }
    }
}