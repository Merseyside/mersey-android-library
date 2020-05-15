package com.merseyside.merseyLib.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.merseyside.merseyLib.presentation.activity.BaseActivity
import com.merseyside.merseyLib.presentation.activity.Orientation
import com.merseyside.merseyLib.presentation.view.IView
import com.merseyside.merseyLib.presentation.view.OrientationHandler
import com.merseyside.merseyLib.presentation.view.localeViews.ILocaleManager
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.SnackbarManager

abstract class BaseDialog : DialogFragment(), IView, OrientationHandler, ILocaleManager {

    protected var data: Bundle? = null

    protected lateinit var baseActivity: BaseActivity

    protected var snackbarManager: SnackbarManager? = null
    override var keyboardUnregistrar: Any? = null
    override var orientation: Orientation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.baseActivity = context
        }
    }

    protected abstract fun performInjection(bundle: Bundle?)

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreate(onSavedInstanceState: Bundle?) {
        super.onCreate(onSavedInstanceState)
        data = arguments

        performInjection(onSavedInstanceState)
    }

    override fun handleError(throwable: Throwable) {
        baseActivity.handleError(throwable)
    }

    override fun setLanguage(lang: String?) {
        baseActivity.setLanguage(lang)
    }

    protected fun getDialogView(): View? {
        return dialog?.window?.decorView
    }

    override fun showAlertDialog(
        title: String?,
        message: String?,
        positiveButtonText: String?,
        negativeButtonText: String?,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit,
        isSingleAction: Boolean?,
        isCancelable: Boolean?
    ) {
        throw UnsupportedOperationException()
    }

    override fun showAlertDialog(
        titleRes: Int?,
        messageRes: Int?,
        positiveButtonTextRes: Int?,
        negativeButtonTextRes: Int?,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit,
        isSingleAction: Boolean?,
        isCancelable: Boolean?
    ) {
        throw UnsupportedOperationException()
    }

    override fun getActualString(id: Int?, vararg args: String): String? {
        return baseActivity.getActualString(id, *args)
    }

    override fun onOrientationChanged(orientation: Orientation, savedInstanceState: Bundle?) {}

    @CallSuper
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = getCancelable()
        snackbarManager = baseActivity.snackbarManager

        setOrientation(resources, savedInstanceState)

        return object: Dialog(context!!, getStyle()) {

            override fun onBackPressed() {
                if (this@BaseDialog.onBackPressed()) {
                    super.onBackPressed()
                }
            }

        }.apply<Dialog> {

            val title = getTitle(context)

            if (title.isNullOrEmpty()) {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
            } else {
                setTitle(title)
            }

            setCanceledOnTouchOutside(getCancelable())

            if (view == null) {
                setContentView(getLayoutId())
            }
        }

    }

    open fun onBackPressed(): Boolean {
        return true
    }

    override fun onStart() {
        super.onStart()

        doLayout()
    }

    open fun doLayout() {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveOrientation(outState)
    }

    open fun getCancelable(): Boolean {
        return true
    }

    @Deprecated("This method doesn't call in dialog classes. Use onCreateDialog()")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    open fun getTitle(context: Context): String? {
        return null
    }

    @StyleRes open fun getStyle(): Int {
        return 0
    }

    override fun show(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)

        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }

        transaction.addToBackStack(null)

        try {
            show(transaction, tag)
        } catch (e: IllegalStateException) {}
    }

    fun hideKeyboard(view: View) {
        baseActivity.hideKeyboard(context, view)
    }

    override fun showMsg(msg: String, view: View?, actionMsg: String?, onClick: () -> Unit) {

        snackbarManager?.apply {
            showSnackbar(
                view = view ?: getDialogView(),
                message = msg,
                actionMsg = actionMsg,
                onClick = onClick
            )
        }
    }

    override fun showErrorMsg(msg: String, view: View?, actionMsg: String?, onClick: () -> Unit) {

        snackbarManager?.apply {
            showErrorSnackbar(
                view = view ?: getDialogView(),
                message = msg,
                actionMsg = actionMsg,
                onClick = onClick
            )
        }
    }

    override fun dismissMsg() {
        if (snackbarManager?.isShowing() == true) {
            snackbarManager!!.dismiss()
        } else {
            Logger.log(this, "Snackbar had not shown")
        }
    }

    override fun getRootView(): View? {
        return getDialogView()
    }

    fun setLayoutPixelSize(width: Int?, height: Int?) {
        val window = dialog!!.window ?: return
        val params: WindowManager.LayoutParams = window.attributes

        if (width != null) params.width = width
        if (height != null) params.height = height

        window.attributes = params
    }

    fun setLayoutSize(@DimenRes widthId: Int? = null, @DimenRes heightId: Int? = null) {
        var width: Int? = null
        var height: Int? = null

        if (widthId != null) width = resources.getDimensionPixelSize(widthId)
        if (heightId != null) height = resources.getDimensionPixelSize(heightId)

        setLayoutPixelSize(width, height)
    }
}