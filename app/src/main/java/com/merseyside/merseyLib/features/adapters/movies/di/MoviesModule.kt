package com.merseyside.merseyLib.features.adapters.movies.di

import android.app.Application
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.merseyLib.features.adapters.movies.model.MoviesViewModel
import com.merseyside.merseyLib.features.adapters.movies.view.MoviesFragment
import dagger.Module
import dagger.Provides

@Module
class MoviesModule(private val fragment: MoviesFragment) {

    @Provides
    fun provideMoviesViewModel(
        @ApplicationContext application: Application,
    ) = fragment.viewModel { MoviesViewModel(application) }

}