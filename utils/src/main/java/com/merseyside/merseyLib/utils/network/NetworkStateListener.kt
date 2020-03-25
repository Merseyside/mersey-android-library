package com.merseyside.merseyLib.utils.network

interface NetworkStateListener {

    fun onConnectionState(state: Boolean)
}