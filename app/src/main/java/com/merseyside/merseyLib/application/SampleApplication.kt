package com.merseyside.merseyLib.application

import com.merseyside.archy.BaseApplication
import com.merseyside.merseyLib.application.di.AppComponent
import com.merseyside.merseyLib.application.di.AppModule
import com.merseyside.merseyLib.application.di.DaggerAppComponent

class SampleApplication : BaseApplication() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = buildComponent()
    }

    private fun buildComponent() =
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

    companion object {
        private lateinit var instance: SampleApplication

        fun getInstance(): SampleApplication {
            return instance
        }
    }
}