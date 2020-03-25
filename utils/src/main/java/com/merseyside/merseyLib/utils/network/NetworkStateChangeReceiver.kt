package com.merseyside.merseyLib.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkStateChangeReceiver() : BroadcastReceiver() {

    private var listener: NetworkStateListener? = null

    fun setStateListener(listener: NetworkStateListener) {
        this.listener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            if (isOnline(context)) {
                listener?.onConnectionState(true)
            } else {
                listener?.onConnectionState(false)
            }
        }
    }
}