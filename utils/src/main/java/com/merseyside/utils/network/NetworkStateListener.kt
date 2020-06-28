package com.merseyside.utils.network

interface NetworkStateListener {

    fun onConnectionState(state: Boolean)
}