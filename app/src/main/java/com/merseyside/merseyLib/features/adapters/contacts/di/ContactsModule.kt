package com.merseyside.merseyLib.features.adapters.contacts.di

import android.app.Application
import androidx.fragment.app.Fragment
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.merseyLib.features.adapters.contacts.model.ContactViewModel
import com.merseyside.merseyLib.features.adapters.contacts.producer.ContactProducer
import dagger.Module
import dagger.Provides

@Module
class ContactsModule(private val fragment: Fragment) {

    @Provides
    fun provideContactsViewModel(
        @ApplicationContext application: Application,
        contactProducer: ContactProducer
    ) = fragment.viewModel { ContactViewModel(application, contactProducer) }

    @Provides
    fun provideContactProducer(): ContactProducer {
        return ContactProducer()
    }
}