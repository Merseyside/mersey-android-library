package com.merseyside.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkStateChangeReceiver : BroadcastReceiver() {

    private var listeners: MutableList<NetworkStateListener> = mutableListOf()

    fun setStateListener(listener: NetworkStateListener) {
        listeners.add(listener)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            if (isOnline(context)) {
                notifyListeners(true)
            } else {
                notifyListeners(false)
            }
        }
    }

    private fun notifyListeners(state: Boolean) {
        listeners.forEach { it.onConnectionState(state) }
    }

    fun removeStateListener(listener: NetworkStateListener) {
        listeners.remove(listener)
    }
}