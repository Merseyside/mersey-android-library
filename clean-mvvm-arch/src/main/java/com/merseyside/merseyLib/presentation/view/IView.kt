package com.merseyside.merseyLib.presentation.view

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.merseyside.merseyLib.presentation.activity.Orientation
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

interface IView {

    fun showMsg(msg: String, actionMsg: String? = null, clickListener: View.OnClickListener? = null)

    fun showErrorMsg(msg: String, actionMsg: String? = null, clickListener: View.OnClickListener? = null)

    fun dismissMsg()

    fun handleError(throwable: Throwable)

    fun setLanguage(lang: String? = null)
    
    fun showAlertDialog(
        title: String? = null,
        message: String? = null,
        positiveButtonText: String? = null,
        negativeButtonText: String? = null,
        onPositiveClick: () -> Unit = {},
        onNegativeClick: () -> Unit = {},
        isOneAction: Boolean? = null,
        isCancelable: Boolean? = null
    )

    fun showAlertDialog(
        @StringRes titleRes: Int? = null,
        @StringRes messageRes: Int? = null,
        @StringRes positiveButtonTextRes: Int? = null,
        @StringRes negativeButtonTextRes: Int? = null,
        onPositiveClick: () -> Unit = {},
        onNegativeClick: () -> Unit = {},
        isOneAction: Boolean? = null,
        isCancelable: Boolean? = null
    )

    fun getActualString(@StringRes id: Int?, vararg args: String): String?

    /**
     * It's a hack! Don't know why but Unregistrar(like his implementations) interface
     * cannot be accessed if had declared as global variable.
     */
    var keyboardUnregistrar: Any?

    fun unregisterKeyboardListener() {
        if (keyboardUnregistrar != null) {
            (keyboardUnregistrar!! as Unregistrar).unregister()
        }
    }

    fun onOrientationChanged(orientation: Orientation, savedInstanceState: Bundle?)
}