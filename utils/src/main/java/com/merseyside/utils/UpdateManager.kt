package com.merseyside.utils

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.install.model.ActivityResult
import com.merseyside.merseyLib.kotlin.logger.Logger
import kotlin.IllegalStateException

class UpdateManager(private val activity: Activity) {

    interface OnAppUpdateListener {
        fun immediateUpdateAvailable()

        fun flexibleUpdateAvailable()

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

    fun setOnAppUpdateListener(onAppUpdateListener: OnAppUpdateListener) {

        this.onAppUpdateListener = onAppUpdateListener

        appUpdateManager.appUpdateInfo.apply {

            addOnFailureListener { Logger.log(TAG, "Fail to get updates") }

            addOnSuccessListener { appUpdateInfo ->
                Logger.log(TAG, "${appUpdateInfo.updateAvailability()}")

                this@UpdateManager.appUpdateInfo = appUpdateInfo

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    if (requestCode != null) {
                        startImmediateUpdate(requestCode!!)
                    }
                } else {
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        onAppUpdateListener.updateDownloaded()
                    } else {
                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                Logger.log(TAG, "Immediate updating is available")
                                this@UpdateManager.onAppUpdateListener?.immediateUpdateAvailable()
                            }

                            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                                Logger.log(TAG, "Flexible updating is available")
                                this@UpdateManager.onAppUpdateListener?.flexibleUpdateAvailable()
                            }
                        } else {
                            Logger.log(TAG, "No updates")
                        }
                    }
                }
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    fun startImmediateUpdate(requestCode: Int) {
        if (isRequestCodeValid(requestCode))  {
            this.requestCode = requestCode

            appUpdateInfo?.let {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.IMMEDIATE,
                    activity,
                    requestCode
                )
            } ?: throw IllegalStateException("App is not available for updating")
        } else throw IllegalArgumentException("requestCode must be lower than 2^16")
    }

    @Throws(IllegalArgumentException::class)
    fun startFlexibleUpdate(requestCode: Int, onFlexibleUpdateStateListener: OnFlexibleUpdateStateListener) {
        if (isRequestCodeValid(requestCode)) {
            this.requestCode = requestCode

            appUpdateInfo?.let {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.FLEXIBLE,
                    activity,
                    requestCode
                )

                val installListener: InstallStateUpdatedListener = object: InstallStateUpdatedListener {

                    override fun onStateUpdate(state: InstallState) {
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

                            else -> {}
                        }

                        if (state.installStatus() != InstallStatus.DOWNLOADED) {
                            appUpdateManager.unregisterListener(this)
                        }

                    }
                }

                appUpdateManager.registerListener(installListener)

            } ?: throw IllegalStateException("App is not available for updating")
        } else throw IllegalArgumentException("requestCode must be lower than 2^16")
    }

    fun installDownloadedUpdate() {
        appUpdateManager.completeUpdate()
    }

    private fun isRequestCodeValid(requestCode: Int): Boolean {
        return requestCode in 0 until Short.MAX_VALUE * 2
    }

    companion object {
        private const val TAG = "UpdateManager"

        const val RESULT_IN_APP_UPDATE_FAILED = ActivityResult.RESULT_IN_APP_UPDATE_FAILED
    }
}