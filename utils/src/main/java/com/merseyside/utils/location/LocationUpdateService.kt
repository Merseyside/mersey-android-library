package com.merseyside.utils.location

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*

class LocationUpdatesService : Service() {

    private val mBinder: IBinder = LocalBinder()

    private var mNotificationManager: NotificationManager? = null

    /**
     * Contains parameters used by [com.google.android.gms.location.FusedLocationProviderApi].
     */
    private lateinit var mLocationRequest: LocationRequest

    /**
     * Provides access to the Fused Location Provider API.
     */
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    /**
     * Callback for changes in location.
     */
    private lateinit var mLocationCallback: LocationCallback
    /**
     * The current location.
     */
    private var mLocation: Location? = null
    override fun onCreate() {
        isRunning = true

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager?.createNotificationChannel(mChannel)
        }

        getLastLocation()
        requestLocationUpdates()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int) = START_NOT_STICKY

    override fun onDestroy() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        mNotificationManager?.cancelAll()
        isRunning = false
    }

    override fun onBind(intent: Intent?) = mBinder

    private fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        startService(Intent(applicationContext, LocationUpdatesService::class.java))
        try {
            Looper.myLooper()?.let {
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    it
                )
            }

            mNotificationManager?.notify(
                NOTIFICATION_ID,
                notification
            )
        } catch (unlikely: SecurityException) {
            Log.e(
                TAG,
                "Lost location permission. Could not request updates. $unlikely"
            )
        }
    }

    private val notification: Notification
        get() {
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(text)
                .setContentTitle(title)
                .setOngoing(true)
                .setSmallIcon(iconRes)
                .setTicker(ticker)
                .setWhen(System.currentTimeMillis())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.priority = NotificationManager.IMPORTANCE_HIGH
            } else {
                builder.priority = Notification.PRIORITY_HIGH
            }

            // Set the Channel ID for Android O.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID) // Channel ID
            }
            return builder.build()
        }

    private fun getLastLocation() {
        try {
            mFusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        Intent(ACTION_BROADCAST).also {
                            it.putExtra(LAST_LOCATION_KEY, task.result)
                            sendBroadcast(it)
                        }
                    } else {
                        Log.w(TAG, "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }

    private fun onNewLocation(location: Location?) {
        Log.i(TAG, "New location: $location")
        mLocation = location

        // Notify anyone listening for broadcasts about the new location.
        Intent(ACTION_BROADCAST).also {
            it.putExtra(LOCATION_KEY, location)
            sendBroadcast(it)
        }
    }

    /**
     * Sets the location request parameters.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    inner class LocalBinder : Binder() {
        val service: LocationUpdatesService
            get() = this@LocationUpdatesService
    }

    companion object {
        internal var name: String = "App name"
        internal var title: String = "Location app"
        internal var text: String = "Getting your location"
        internal var ticker: String = ""
        internal var iconRes: Int = android.R.mipmap.sym_def_app_icon

        var isRunning = false

        private const val PACKAGE_NAME = "com.merseyside.utils"
        private val TAG = LocationUpdatesService::class.java.simpleName

        const val ACTION_BROADCAST = "$PACKAGE_NAME.LOCATION_BROADCAST"

        const val LOCATION_KEY = "location"
        const val LAST_LOCATION_KEY = "last_location"

        /**
         * The name of the channel for notifications.
         */
        private const val CHANNEL_ID = "channel_01"
        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

        /**
         * The fastest rate for active location updates. Updates will never be more frequent
         * than this value.
         */
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2

        /**
         * The identifier for the notification displayed for the foreground service.
         */
        private const val NOTIFICATION_ID = 12345678
    }
}