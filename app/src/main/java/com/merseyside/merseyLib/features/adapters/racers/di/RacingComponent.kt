package com.merseyside.merseyLib.features.adapters.racers.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.features.adapters.colors.di.ColorsModule
import com.merseyside.merseyLib.features.adapters.racers.view.RacingFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [RacingModule::class])
interface RacingComponent {

    fun inject(fragment: RacingFragment)
}