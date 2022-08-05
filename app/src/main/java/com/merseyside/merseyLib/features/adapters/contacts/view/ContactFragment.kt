package com.merseyside.merseyLib.features.adapters.contacts.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import com.merseyside.adapters.utils.UpdateRequest
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentContactsBinding
import com.merseyside.merseyLib.features.adapters.contacts.adapter.ContactExpandableAdapter
import com.merseyside.merseyLib.features.adapters.contacts.di.ContactsModule
import com.merseyside.merseyLib.features.adapters.contacts.di.DaggerContactsComponent
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactViewModel
import com.merseyside.utils.view.ext.onClick

class ContactFragment: BaseSampleFragment<FragmentContactsBinding, ContactViewModel>() {

    private val adapter = ContactExpandableAdapter()

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

        requireBinding().recycler.adapter = adapter
        viewModel.contactsFlow.asLiveData().observe(viewLifecycleOwner) {
            adapter.update(UpdateRequest(it))
        }

        requireBinding().populate.onClick {
            viewModel.populate()
        }
    }
}