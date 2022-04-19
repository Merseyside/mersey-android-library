package com.merseyside.utils.network

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresPermission
import com.merseyside.utils.mainThread
import com.merseyside.utils.singletons.SingletonHolder

@SuppressLint("MissingPermission")
class InternetConnectionObserver private constructor(private val context: Context) {
    private var broadcastReceiver: NetworkStateChangeReceiver? = null
    private var listeners: MutableList<NetworkStateListener> = mutableListOf()

    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                mainThread { notifyListeners(true) }
            }

            override fun onLost(network: Network) {
                mainThread { notifyListeners(false) }
            }
        }
    }

    fun addNetworkStateListener(listener: NetworkStateListener) {
        if (listeners.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getConnectivityManager(context).registerDefaultNetworkCallback(networkCallback)
            } else if (broadcastReceiver == null) {
                broadcastReceiver = NetworkStateChangeReceiver().apply {
                    setStateListener(listener)

                    val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
                    context.registerReceiver(this, intentFilter)
                }
            }
        }

        listeners.add(listener)
        listener.onConnectionState(isOnline(context))
    }

    fun removeNetworkStateListener(listener: NetworkStateListener) {
        if (listeners.isEmpty()) throw IllegalStateException("No receivers registered!")

        listeners.remove(listener)
        broadcastReceiver?.removeStateListener(listener)

        if (listeners.isEmpty()) {
            unregisterReceiver()
        }
    }

    fun removeNetworkStateListeners() {
        listeners.forEach { removeNetworkStateListener(it) }
    }

    private fun unregisterReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getConnectivityManager(context).unregisterNetworkCallback(networkCallback)
        } else {
            broadcastReceiver?.let {
                context.unregisterReceiver(broadcastReceiver)
                broadcastReceiver = null
            }
        }
    }

    private fun notifyListeners(state: Boolean) {
        listeners.forEach { it.onConnectionState(state) }
    }

    fun isOnline(): Boolean {
        return isOnline(context)
    }

    fun isWifiOnAndConnected(): Boolean {
        return isWifiOnAndConnected(context)
    }

    companion object : SingletonHolder<InternetConnectionObserver, Context>(::InternetConnectionObserver) {
        @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        fun isOnline(context: Context): Boolean {
            val cm = getConnectivityManager(context)

            return cm.activeNetworkInfo?.isConnected ?: false
        }

        @RequiresPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
        fun isWifiOnAndConnected(context: Context): Boolean {
            val wifiMgr =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

            return if (wifiMgr.isWifiEnabled) {
                val wifiInfo = wifiMgr.connectionInfo
                wifiInfo.networkId != -1
            } else {
                false
            }
        }

        fun getConnectivityManager(context: Context): ConnectivityManager {
            return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
    }

}