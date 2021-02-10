package com.merseyside.merseyLib.application.di

import android.app.Application
import android.content.Context
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    @ApplicationContext
    fun application(): Application

    @ApplicationContext
    fun context(): Context
}