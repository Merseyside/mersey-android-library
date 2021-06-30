package com.merseyside.utils.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.merseyside.utils.isPermissionsGranted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationManagerImpl(private val context: Context) : LocationManager, LifecycleObserver {

    private val isRunning: Boolean
        get() {
            return LocationUpdatesService.isRunning
        }

    private val intent = Intent(context, LocationUpdatesService::class.java)
    private var broadcastReceiver: LocationBroadcastReceiver? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        registerReceiver()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        stopAndUnregisterReceiver()
    }

    @ExperimentalCoroutinesApi
    override fun getLocationFlow() = callbackFlow<Location> {
        startService()

        broadcastReceiver?.addCallback(object : LocationBroadcastReceiver.LocationCallback {
            override fun onReceive(location: Location) {
                trySend(location)
            }
        })

        awaitClose {
            stopService()
        }
    }

    override suspend fun getLastLocation(): Location = suspendCancellableCoroutine { continuation ->
        startService()

        broadcastReceiver?.addLastLocationCallback(object :
            LocationBroadcastReceiver.LocationCallback {
            override fun onReceive(location: Location) {
                broadcastReceiver?.removeCallback(this)
                continuation.resume(location)
                stopService()
            }
        })
    }

    override suspend fun getLocation(): Location = suspendCancellableCoroutine { continuation ->
        startService()

        broadcastReceiver?.addCallback(object : LocationBroadcastReceiver.LocationCallback {
            override fun onReceive(location: Location) {
                broadcastReceiver?.removeCallback(this)
                continuation.resume(location)
                stopService()
            }
        })
    }

    private fun registerReceiver() {
        if (broadcastReceiver == null) {
            broadcastReceiver = LocationBroadcastReceiver()

            val filter = IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
            context.registerReceiver(broadcastReceiver, filter)
        }
    }

    private fun unregisterReceiver() {
        broadcastReceiver?.let {
            context.unregisterReceiver(broadcastReceiver)
            broadcastReceiver = null
        }
    }

    private fun startService() {
        if (!isRunning) {
            context.startService(intent)
        }
    }

    private fun stopService() {
        if (isRunning) {
            context.stopService(intent)
        }
    }

    private fun stopAndUnregisterReceiver() {
        unregisterReceiver()
        stopService()
    }

    override fun hasRequestedPermissions(): Boolean {
        with(context) {
            return isPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION) ||
                isPermissionsGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    fun setNotificationText(text: String) {
        LocationUpdatesService.text = text
    }

    fun setNotificationTitle(title: String) {
        LocationUpdatesService.title = title
    }

    fun setNotificationIcon(iconRes: Int) {
        LocationUpdatesService.iconRes = iconRes
    }

    fun setNotificationTicker(ticker: String) {
        LocationUpdatesService.ticker = ticker
    }

    fun setNotificationName(name: String) {
        LocationUpdatesService.name = name
    }
}