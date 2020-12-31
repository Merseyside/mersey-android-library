package com.merseyside.merseyLib.features.adapters.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.features.adapters.view.AdaptersFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [AdaptersModule::class])
interface AdaptersComponent {

    fun inject(fragment: AdaptersFragment)
}