package com.merseyside.merseyLib.features.adapters.concat.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.features.adapters.concat.view.ConcatAdapterFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [ConcatModule::class])
interface ConcatComponent {

    fun inject(fragment: ConcatAdapterFragment)
}