package com.merseyside.utils.service

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationManagerImpl(private val context: Context) : LocationManager, LifecycleObserver {

    private val isRunning: Boolean
        get() {
            return LocationUpdatesService.isRunning
        }

    private var broadcastReceiver: LocationBroadcastReceiver? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        registerReceiver()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeReceiver()
    }

    override suspend fun getLocation(): Location = suspendCancellableCoroutine { continuation ->
        if (!isRunning) {
            context.startService(Intent(context, LocationUpdatesService::class.java))

            continuation.invokeOnCancellation { broadcastReceiver?.removeCallback() }
            broadcastReceiver?.addCallback(object : LocationBroadcastReceiver.LocationCallback {
                override fun onReceive(location: Location) {
                    continuation.resume(location)
                    broadcastReceiver?.removeCallback()
                }
            })
        }
    }

    private fun registerReceiver() {
        if (broadcastReceiver == null) {
            broadcastReceiver = LocationBroadcastReceiver()

            val filter = IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
            context.registerReceiver(broadcastReceiver, filter)
        }
    }

    private fun removeReceiver() {
        context.unregisterReceiver(broadcastReceiver)
        broadcastReceiver = null
    }
}