package com.merseyside.merseyLib.utils.network

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresPermission
import com.merseyside.merseyLib.utils.mainThread

class InternetConnectionObserver(private val context: Context) {

    private var broadcastReceiver: NetworkStateChangeReceiver? = null

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun registerReceiver(listener: NetworkStateListener) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            getConnectivityManager(context).registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    mainThread {
                        listener.onConnectionState(true)
                    }
                }

                override fun onLost(network: Network?) {
                    mainThread {
                        listener.onConnectionState(false)
                    }
                }
            })
        } else {
            broadcastReceiver = NetworkStateChangeReceiver()
            broadcastReceiver!!.setStateListener(listener)

            val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
            context.registerReceiver(broadcastReceiver!!, intentFilter)
        }
    }

    @Deprecated("On API >= 24 DefaultNetworkCallback is used and doesn't need to be unregistered")
    fun unregisterReceiver() {
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver)
            broadcastReceiver = null
        }
    }

    companion object {
        @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        fun isOnline(context: Context): Boolean {
            val cm = getConnectivityManager(context)

            return cm.activeNetworkInfo?.isConnected ?: false
        }

        @RequiresPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
        fun isWifiOnAndConnected(context: Context): Boolean {
            val wifiMgr = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

            return if (wifiMgr.isWifiEnabled) {
                val wifiInfo = wifiMgr.connectionInfo
                wifiInfo.networkId != -1
            } else {
                false
            }
        }

        private fun getConnectivityManager(context: Context): ConnectivityManager {
            return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
    }

}