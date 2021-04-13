package com.merseyside.utils.service

import android.location.Location

interface LocationManager {

    suspend fun getLocation(): Location
}