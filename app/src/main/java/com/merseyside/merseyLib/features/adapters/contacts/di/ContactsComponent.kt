package com.merseyside.merseyLib.features.adapters.contacts.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.features.adapters.contacts.view.ContactFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [ContactsModule::class])
interface ContactsComponent {

    fun inject(fragment: ContactFragment)
}