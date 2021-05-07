package com.merseyside.utils.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationManager {

    suspend fun getLocation(): Location

    fun getLocationFlow(): Flow<Location>

    suspend fun getLastLocation(): Location

    fun hasRequestedPermissions(): Boolean
}