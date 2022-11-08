package com.merseyside.merseyLib.features.adapters.contacts.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import com.merseyside.adapters.feature.expanding.Expanding
import com.merseyside.adapters.feature.filtering.Filtering
import com.merseyside.adapters.feature.filtering.ext.addAndApply
import com.merseyside.adapters.feature.filtering.ext.removeAndApply
import com.merseyside.adapters.feature.selecting.group.SelectingGroup
import com.merseyside.adapters.feature.sorting.Sorting
import com.merseyside.adapters.feature.selecting.SelectableMode
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentContactsBinding
import com.merseyside.merseyLib.features.adapters.contacts.adapter.ContactNestedAdapter
import com.merseyside.merseyLib.features.adapters.contacts.adapter.ContactsComparator
import com.merseyside.merseyLib.features.adapters.contacts.adapter.ContactsNestedAdapterFilter
import com.merseyside.merseyLib.features.adapters.contacts.di.ContactsModule
import com.merseyside.merseyLib.features.adapters.contacts.di.DaggerContactsComponent
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactViewModel
import com.merseyside.utils.view.ext.addTextChangeListener
import com.merseyside.utils.view.ext.onClick

class ContactFragment : BaseSampleFragment<FragmentContactsBinding, ContactViewModel>() {

    private val contactsFilter = ContactsNestedAdapterFilter()

    private val adapter = ContactNestedAdapter {
        Sorting {
            comparator = ContactsComparator
        }

        Filtering {
            filter = contactsFilter
        }

        SelectingGroup {
            selectableMode = SelectableMode.SINGLE
        }

        Expanding {
            variableId = BR.expandCallback
        }
    }

    private val textChangeListener = {
            view: View,
            newValue: String?,
            _: String?,
            length: Int,
            _: Int,
            _: Int,
            _: Int ->

        newValue?.let { value ->
            if (value.isNotEmpty()) {
                contactsFilter.addAndApply(ContactsNestedAdapterFilter.QUERY_KEY, newValue)
            } else {
                contactsFilter.removeAndApply(ContactsNestedAdapterFilter.QUERY_KEY)
            }
        }

        true
    }

    override fun hasTitleBackButton() = true
    override fun getLayoutId() = R.layout.fragment_contacts
    override fun getTitle(context: Context) = "Contacts"
    override fun getBindingVariable() = BR.viewModel

    override fun performInjection(bundle: Bundle?, vararg params: Any) {
        DaggerContactsComponent.builder()
            .appComponent(appComponent)
            .contactsModule(getContactModule(bundle))
            .build().inject(this)
    }

    private fun getContactModule(bundle: Bundle?) = ContactsModule(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireBinding().filter.addTextChangeListener(textChangeListener)

        requireBinding().recycler.adapter = adapter
        viewModel.contactsFlow.asLiveData().observe(viewLifecycleOwner) { items ->
            adapter.addOrUpdateAsync(items)
        }

        requireBinding().populate.onClick {
            viewModel.populate()
        }
    }
}