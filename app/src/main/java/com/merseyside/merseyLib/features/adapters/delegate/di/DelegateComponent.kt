package com.merseyside.merseyLib.features.adapters.delegate.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.features.adapters.delegate.view.DelegateFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [DelegateModule::class])
interface DelegateComponent {

    fun inject(fragment: DelegateFragment)
}