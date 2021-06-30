package com.merseyside.merseyLib.features.location.di

import android.app.Application
import android.content.Context
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.merseyLib.features.location.model.LocationViewModel
import com.merseyside.merseyLib.features.location.view.LocationFragment
import com.merseyside.utils.location.LocationManager
import com.merseyside.utils.location.LocationManagerImpl
import dagger.Module
import dagger.Provides

@Module
class LocationModule(
    private val fragment: LocationFragment,
) {

    @Provides
    fun provideLocationViewModel(
        @ApplicationContext application: Application
    ) = fragment.viewModel { LocationViewModel(application) }

    @Provides
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager {
        return LocationManagerImpl(context).apply {
            fragment.lifecycle.addObserver(this)

            setNotificationText("Testing foreground location")
            setNotificationName("Sample")
        }
    }

}