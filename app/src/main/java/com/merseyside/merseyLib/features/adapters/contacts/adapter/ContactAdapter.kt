package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.config
import com.merseyside.adapters.feature.filter.Filtering
import com.merseyside.adapters.single.SimpleAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactItemViewModel

class ContactAdapter(
    config: AdapterConfig<String, ContactItemViewModel>
): SimpleAdapter<String, ContactItemViewModel>(config) {

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_contact
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: String) = ContactItemViewModel(item)

    companion object {
        operator fun invoke(): ContactAdapter {
            return ContactAdapter(config<String, ContactItemViewModel> {
                Filtering {
                    filter = ContactsFilter()
                }
            })
        }
    }

}