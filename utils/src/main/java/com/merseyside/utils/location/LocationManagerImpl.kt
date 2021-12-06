package com.merseyside.utils.location

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import androidx.lifecycle.*
import com.merseyside.utils.isPermissionsGranted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationManagerImpl(private val context: Context) : LocationManager,
    DefaultLifecycleObserver {

    private val isRunning: Boolean
        get() = LocationUpdatesService.isRunning

    private val intent = Intent(context, LocationUpdatesService::class.java)
    private var broadcastReceiver: LocationBroadcastReceiver? = null

    override fun onCreate(owner: LifecycleOwner) {
        registerReceiver()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stopAndUnregisterReceiver()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLocationFlow() = callbackFlow {
        registerReceiver()
        broadcastReceiver?.addCallback(object : LocationBroadcastReceiver.LocationCallback {
            override fun onReceive(location: Location) {
                trySend(location)
            }
        })

        startService()
        awaitClose {
            stopService()
        }
    }

    override suspend fun getLastLocation(): Location = suspendCancellableCoroutine { continuation ->
        registerReceiver()
        broadcastReceiver?.addLastLocationCallback(object :
            LocationBroadcastReceiver.LocationCallback {
            override fun onReceive(location: Location) {
                broadcastReceiver?.removeCallback(this)
                continuation.resume(location)
                stopService()
            }
        })

        startService()
    }

    override suspend fun getLocation(): Location = suspendCancellableCoroutine { continuation ->
        registerReceiver()
        broadcastReceiver?.addCallback(object : LocationBroadcastReceiver.LocationCallback {
            override fun onReceive(location: Location) {
                broadcastReceiver?.removeCallback(this)
                continuation.resume(location)
                stopService()
            }
        })

        startService()
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
        return with(context) {
            isPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION) ||
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