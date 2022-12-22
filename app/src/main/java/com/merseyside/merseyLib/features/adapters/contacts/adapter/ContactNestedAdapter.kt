package com.merseyside.merseyLib.features.adapters.contacts.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.merseyside.adapters.config.NestedAdapterConfig
import com.merseyside.adapters.extensions.onClick
import com.merseyside.adapters.single.NestedAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.databinding.ItemGroupContactBinding
import com.merseyside.merseyLib.features.adapters.contacts.entity.ContactGroup
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactGroupItemViewModel
import com.merseyside.adapters.config.init.initNestedAdapter
import com.merseyside.adapters.interfaces.ext.removeAsync

class ContactNestedAdapter(config: ContactNestedAdapterConfig) : NestedAdapter<ContactGroup, ContactGroupItemViewModel,
        String, ContactAdapter>(config) {

    init {
        onClick {
            removeAsync(it)
        }
    }

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_group_contact

    override fun getBindingVariable() = BR.model

    override fun createItemViewModel(item: ContactGroup) = ContactGroupItemViewModel(item)

    override fun initNestedAdapter(model: ContactGroupItemViewModel) = ContactAdapter()

    override fun getNestedView(binding: ViewDataBinding): RecyclerView {
        return (binding as ItemGroupContactBinding).recycler
    }

    companion object {
        operator fun invoke(
            configure: ContactNestedAdapterConfig.() -> Unit
        ): ContactNestedAdapter {
            return initNestedAdapter(::ContactNestedAdapter, configure)
        }
    }
}

private typealias ContactNestedAdapterConfig = NestedAdapterConfig<ContactGroup, ContactGroupItemViewModel,
        String, ContactAdapter>