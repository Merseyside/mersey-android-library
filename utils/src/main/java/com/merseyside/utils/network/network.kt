package com.merseyside.utils.network

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("MissingPermission")
fun isOnline(context: Context): Boolean {
    return InternetConnectionObserver.isOnline(context)
}

@SuppressLint("MissingPermission")
fun isWifiOnAndConnected(context: Context): Boolean {
    return InternetConnectionObserver.isOnline(context)
}