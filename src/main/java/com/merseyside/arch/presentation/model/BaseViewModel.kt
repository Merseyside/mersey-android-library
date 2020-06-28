package com.merseyside.archy.model

import android.content.Context
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.merseyside.archy.interfaces.IStringHelper
import com.merseyside.utils.Logger
import com.merseyside.utils.PermissionManager
import com.merseyside.utils.mvvm.SingleLiveEvent
import com.merseyside.utils.ext.clear

abstract class BaseViewModel protected constructor() : ViewModel(), IStringHelper {

    val isInProgress = ObservableBoolean(false)
    val progressText = ObservableField<String>()

    val errorLiveEvent: MutableLiveData<Throwable> =
        SingleLiveEvent()

    val messageLiveEvent: MutableLiveData<TextMessage> =
        SingleLiveEvent()

    val isInProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val alertDialogLiveEvent: MutableLiveData<AlertDialogModel> =
        SingleLiveEvent()

    val grantPermissionLiveEvent: MutableLiveData<Pair<Array<String>, Int>> =
        SingleLiveEvent()

    data class TextMessage(
        val isError: Boolean = false,
        var msg: String = "",
        var actionMsg: String? = null,
        val onClick: () -> Unit = {}
    )

    data class AlertDialogModel(
        val title: String? = null,
        val message: String? = null,
        val positiveButtonText: String? = null,
        val negativeButtonText: String? = null,
        val onPositiveClick: () -> Unit = {},
        val onNegativeClick: () -> Unit = {},
        val isSingleAction: Boolean? = null,
        val isCancelable: Boolean? = null
    )

    open fun handleError(throwable: Throwable) {
        errorLiveEvent.value = throwable
    }

    protected fun showMsg(@StringRes id: Int, vararg args: String) {
        showMsg(getString(id, *args))
    }

    protected fun showErrorMsg(@StringRes id: Int, vararg args: String) {
        showErrorMsg(getString(id, *args))
    }

    protected fun showMsg(msg: String) {
        Logger.log(this, msg)
        val textMessage = TextMessage(
            isError = false,
            msg = msg
        )

        messageLiveEvent.value = textMessage
    }

    protected fun showErrorMsg(msg: String) {
        Logger.logErr(this, msg)
        val textMessage = TextMessage(
            isError = true,
            msg = msg
        )

        messageLiveEvent.value = textMessage
    }

    protected fun showMsg(msg: String, actionMsg: String, onClick: () -> Unit = {}) {
        Logger.log(this, msg)
        val textMessage = TextMessage(
            isError = false,
            msg = msg,
            actionMsg = actionMsg,
            onClick = onClick
        )

        messageLiveEvent.value = textMessage
    }

    protected fun showErrorMsg(msg: String, actionMsg: String, onClick: () -> Unit = {}) {
        Logger.logErr(this, msg)
        val textMessage = TextMessage(
            isError = true,
            msg = msg,
            actionMsg = actionMsg,
            onClick = onClick
        )

        messageLiveEvent.value = textMessage
    }

    open fun onError(throwable: Throwable) {}

    fun showProgress(text: String? = null) {
        Logger.log(this, text ?: "Empty")

        isInProgress.set(true)
        progressText.set(text)

        isInProgressLiveData.value = true
    }

    fun hideProgress() {
        if (isInProgressLiveData.value == true) {
            isInProgress.set(false)
            progressText.clear()

            isInProgressLiveData.value = false
        }
    }

    fun showAlertDialog(
        title: String? = null,
        message: String? = null,
        positiveButtonText: String? = null,
        negativeButtonText: String? = null,
        onPositiveClick: () -> Unit = {},
        onNegativeClick: () -> Unit = {},
        isSingleAction: Boolean? = null,
        isCancelable: Boolean? = null
    ) {
        alertDialogLiveEvent.value = AlertDialogModel(
            title, message, positiveButtonText, negativeButtonText, onPositiveClick, onNegativeClick, isSingleAction, isCancelable
        )
    }

    fun showAlertDialog(
        @StringRes titleRes: Int? = null,
        @StringRes messageRes: Int? = null,
        @StringRes positiveButtonTextRes: Int? = null,
        @StringRes negativeButtonTextRes: Int? = null,
        onPositiveClick: () -> Unit = {},
        onNegativeClick: () -> Unit = {},
        isSingleAction: Boolean? = null,
        isCancelable: Boolean? = null
    ) {

        showAlertDialog(
            getString(titleRes),
            getString(messageRes),
            getString(positiveButtonTextRes),
            getString(negativeButtonTextRes),
            onPositiveClick,
            onNegativeClick,
            isSingleAction,
            isCancelable
        )
    }

    protected abstract fun dispose()

    fun stopAllWorks() {
        dispose()
    }

    fun isPermissionsGranted(context: Context, vararg permissions: String): Boolean {
        return PermissionManager.isPermissionsGranted(context, *permissions)
    }

    fun requestPermissions(permissions: Pair<Array<String>, Int>) {
        grantPermissionLiveEvent.value = permissions
    }

    open fun updateLanguage(context: Context) {}

    open fun onBackPressed() : Boolean {
        return true
    }

    override fun onCleared() {
        super.onCleared()
        dispose()
    }
}
