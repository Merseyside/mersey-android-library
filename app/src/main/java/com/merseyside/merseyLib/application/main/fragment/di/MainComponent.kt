package com.merseyside.merseyLib.application.main.fragment.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.application.main.fragment.view.MainFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [MainModule::class])
interface MainComponent {

    fun inject(fragment: MainFragment)
}