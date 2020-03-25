package com.merseyside.merseyLib.utils

import android.app.Activity
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.install.model.ActivityResult
import kotlin.IllegalStateException

class UpdateManager(private val activity: Activity) {

    interface OnAppUpdateListener {
        fun updateAvailable()
        fun updateDownloaded()
    }

    interface OnFlexibleUpdateStateListener {
        fun onDownloaded()
        fun onFailed()
        fun onCanceled()
        fun onInstalled()
    }

    private var onAppUpdateListener: OnAppUpdateListener? = null

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
    private var appUpdateInfo: AppUpdateInfo? = null

    private var requestCode: Int? = null

    fun setOnAppUpdateListener(onAppUpdateListener: OnAppUpdateListener?) {
        if (onAppUpdateListener != null) {
            this.onAppUpdateListener = onAppUpdateListener

            val appUpdateInfoTask = appUpdateManager.appUpdateInfo

            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                Logger.log(TAG, "${appUpdateInfo.updateAvailability()}")

                this.appUpdateInfo = appUpdateInfo

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    if (requestCode != null) {
                        startImmediateUpdate(requestCode!!)
                    }
                } else {
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        onAppUpdateListener.updateDownloaded()
                    } else {
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                        ) {
                            Log.d(TAG, "Updating is available")
                            this.onAppUpdateListener?.updateAvailable()
                        }
                    }
                }
            }
        } else {
            this.onAppUpdateListener = null
        }
    }

    @Throws(IllegalArgumentException::class)
    fun startImmediateUpdate(requestCode: Int) {
        if (isRequestCodeValid(requestCode))  {
            this.requestCode = requestCode

            if (appUpdateInfo != null) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    activity,
                    requestCode
                )
            } else {
                throw IllegalStateException("App is not available for updating")
            }
        } else throw IllegalArgumentException("requestCode must be lower than 2^16")
    }

    @Throws(IllegalArgumentException::class)
    fun startFlexibleUpdate(requestCode: Int, onFlexibleUpdateStateListener: OnFlexibleUpdateStateListener) {
        if (isRequestCodeValid(requestCode)) {
            this.requestCode = requestCode

            if (appUpdateInfo != null) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    activity,
                    requestCode
                )

                appUpdateManager.registerListener { state ->
                    when (state.installStatus()) {
                        InstallStatus.DOWNLOADED -> {
                            onFlexibleUpdateStateListener.onDownloaded()
                        }

                        InstallStatus.FAILED -> {
                            onFlexibleUpdateStateListener.onFailed()
                        }

                        InstallStatus.CANCELED -> {
                            onFlexibleUpdateStateListener.onCanceled()
                        }

                        InstallStatus.INSTALLED -> {
                            onFlexibleUpdateStateListener.onInstalled()
                        }

                        else -> {
                        }
                    }
                }
            } else {
                throw IllegalStateException("App is not available for updating")
            }
        } else throw IllegalArgumentException("requestCode must be lower than 2^16")
    }

    fun installDownloadedUpdate() {
        appUpdateManager.completeUpdate()
        RESULT_IN_APP_UPDATE_FAILED
    }

    private fun isRequestCodeValid(requestCode: Int): Boolean {
        return requestCode in 0 until Short.MAX_VALUE * 2
    }

    companion object {
        private const val TAG = "UpdateManager"

        const val RESULT_IN_APP_UPDATE_FAILED = ActivityResult.RESULT_IN_APP_UPDATE_FAILED
    }
}