package com.merseyside.merseyLib.features.adapters.contacts.adapter

import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.config.init.initAdapter
import com.merseyside.adapters.feature.filtering.Filtering
import com.merseyside.adapters.feature.selecting.SelectState
import com.merseyside.adapters.feature.selecting.Selecting
import com.merseyside.adapters.feature.selecting.SelectableMode
import com.merseyside.adapters.feature.sorting.Comparator
import com.merseyside.adapters.feature.sorting.Sorting
import com.merseyside.adapters.single.SimpleAdapter
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactItemViewModel

class ContactAdapter(
    config: AdapterConfig<String, ContactItemViewModel>
): SimpleAdapter<String, ContactItemViewModel>(config) {

    override fun getLayoutIdForPosition(position: Int) = R.layout.item_contact
    override fun getBindingVariable() = BR.model
    override fun createItemViewModel(item: String) = ContactItemViewModel(item, SelectState())

    companion object {
        operator fun invoke(): ContactAdapter {
            return initAdapter(::ContactAdapter) {
                Filtering {
                    filter = ContactsFilter()
                }

                Sorting {
                    comparator = object : Comparator<String, ContactItemViewModel>() {
                        override fun compare(
                            model1: ContactItemViewModel,
                            model2: ContactItemViewModel
                        ): Int {
                            return model1.item.compareTo(model2.item)
                        }

                    }
                }

                Selecting {
                    selectableMode = SelectableMode.MULTIPLE
                    onSelect = { item, isSelected, isSelectedByUser ->
                        //item.log("onSelect", suffix = "$isSelected")
                    }
                }
            }
        }
    }

}