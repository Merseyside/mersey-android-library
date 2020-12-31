package com.merseyside.merseyLib.application.di

import android.app.Application
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @ApplicationContext
    @Provides
    fun provideApplication(): Application {
        return application
    }
}