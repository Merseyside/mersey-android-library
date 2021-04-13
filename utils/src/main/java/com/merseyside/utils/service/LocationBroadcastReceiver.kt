package com.merseyside.utils.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location

class LocationBroadcastReceiver : BroadcastReceiver() {

    private var callback: LocationCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == LocationUpdatesService.ACTION_BROADCAST) {
                intent.getParcelableExtra<Location>(LocationUpdatesService.LOCATION_KEY)?.also {
                    callback?.onReceive(it)
                }
            }
        }
    }

    fun addCallback(callback: LocationCallback) {
        this.callback = callback
    }

    fun removeCallback() {
        callback = null
    }

    interface LocationCallback {
        fun onReceive(location: Location)
    }
}