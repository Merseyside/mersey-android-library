package com.merseyside.merseyLib.features.adapters.movies.di

import com.merseyside.archy.presentation.di.qualifiers.FragmentScope
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.features.adapters.movies.view.MoviesFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [MoviesModule::class])
interface MoviesComponent {

    fun inject(fragment: MoviesFragment)
}