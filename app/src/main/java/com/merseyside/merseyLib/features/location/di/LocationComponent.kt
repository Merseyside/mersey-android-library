package com.merseyside.merseyLib.features.location.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.features.location.view.LocationFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [LocationModule::class])
interface LocationComponent {

    fun inject(fragment: LocationFragment)
}