package com.merseyside.merseyLib.features.adapters.colors.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.features.adapters.colors.view.ColorsFragment
import com.merseyside.merseyLib.features.adapters.racers.di.RacingModule
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [ColorsModule::class])
interface ColorsComponent {

    fun inject(fragment: ColorsFragment)
}