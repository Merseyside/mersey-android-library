package com.merseyside.merseyLib.application.main.fragment.di

import android.app.Application
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.merseyLib.application.main.fragment.model.MainViewModel
import com.merseyside.merseyLib.application.main.fragment.view.MainFragment
import dagger.Module
import dagger.Provides

@Module
class MainModule(private val fragment: MainFragment) {

    @Provides
    fun provideMainViewModel(@ApplicationContext application: Application): MainViewModel {
        return fragment.viewModel { MainViewModel(application) }
    }
}