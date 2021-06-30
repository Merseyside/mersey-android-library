package com.merseyside.utils.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location

class LocationBroadcastReceiver : BroadcastReceiver() {

    private var callback: LocationCallback? = null
    private var lastCallback: LocationCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == LocationUpdatesService.ACTION_BROADCAST) {
                intent.getParcelableExtra<Location>(LocationUpdatesService.LOCATION_KEY)?.also {
                    callback?.onReceive(it)
                }

                intent.getParcelableExtra<Location>(LocationUpdatesService.LAST_LOCATION_KEY)?.also {
                    lastCallback?.onReceive(it)
                }
            }
        }
    }

    fun addCallback(callback: LocationCallback) {
        this.callback = callback
    }

    fun addLastLocationCallback(callback: LocationCallback) {
        this.lastCallback = callback
    }

    fun removeCallback(callback: LocationCallback) {
        if (this.callback == callback) this.callback = null
        else if (this.lastCallback == callback) this.lastCallback = null
    }

    interface LocationCallback {
        fun onReceive(location: Location)
    }
}