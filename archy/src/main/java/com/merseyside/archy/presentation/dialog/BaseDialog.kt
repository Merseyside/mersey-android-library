package com.merseyside.archy.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.merseyside.archy.presentation.activity.BaseActivity
import com.merseyside.archy.presentation.activity.Orientation
import com.merseyside.archy.presentation.view.IView
import com.merseyside.archy.presentation.view.OrientationHandler
import com.merseyside.archy.presentation.view.localeViews.ILocaleManager
import com.merseyside.archy.utils.SnackbarManager
import com.merseyside.merseyLib.kotlin.logger.Logger

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

    protected abstract fun performInjection(bundle: Bundle?, vararg params: Any)

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun doLayout() {}
    open fun getCancelable(): Boolean = true
    open fun getTitle(context: Context): String? = null
    @StyleRes
    open fun getStyle(): Int = 0
    open fun onBackPressed(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        performInjection(savedInstanceState)
        data = arguments
        super.onCreate(savedInstanceState)
    }

    protected fun getDialogView(): View? {
        return dialog?.window?.decorView
    }

    @CallSuper
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = getCancelable()
        snackbarManager = baseActivity.snackbarManager

        setOrientation(resources, savedInstanceState)

        return object : Dialog(requireContext(), getStyle()) {

            init {
                requireActivity()
                val title = getTitle(context)

                if (title.isNullOrEmpty()) requestWindowFeature(Window.FEATURE_NO_TITLE)
                else setTitle(title)

                setCanceledOnTouchOutside(getCancelable())
                setView(this)
            }
        }
    }

    protected open fun setView(dialog: Dialog, @LayoutRes layoutId: Int = getLayoutId()) {
        dialog.setContentView(layoutId)
    }

    override fun onStart() {
        super.onStart()
        doLayout()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveOrientation(outState)
    }

    @Deprecated("This method doesn't call in dialog classes. Use onCreateDialog()")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun show(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)

        prevFragment?.let {
            transaction.remove(prevFragment)
        }

        transaction.addToBackStack(null)

        try {
            show(transaction, tag)
        } catch (e: IllegalStateException) {
        }
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

    override fun handleError(throwable: Throwable): Boolean {
        return baseActivity.handleError(throwable)
    }

    override fun setLanguage(lang: String?) {
        baseActivity.setLanguage(lang)
    }

    fun setLayoutSize(width: Int? = null, height: Int? = null) {
        dialog?.window?.apply {
            attributes = attributes.apply {
                if (width != null) this.width = width
                if (height != null) this.height = height
            }
        } ?: throw IllegalStateException("Dialog is null!")
    }

    fun setLayoutDimenSize(
        @DimenRes widthId: Int? = null,
        @DimenRes heightId: Int? = null
    ) {
        var width: Int? = null
        var height: Int? = null

        if (widthId != null) width = resources.getDimensionPixelSize(widthId)
        if (heightId != null) height = resources.getDimensionPixelSize(heightId)

        setLayoutSize(width, height)
    }
}