package com.merseyside.merseyLib.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.merseyside.merseyLib.presentation.activity.BaseActivity

abstract class BaseDialog : DialogFragment() {

    protected var data: Bundle? = null

    protected lateinit var baseActivity: BaseActivity

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

    @CallSuper
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(context!!, getStyle()).apply {

            val title = getTitle(context)

            if (title.isNullOrEmpty()) {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
            } else {
                setTitle(title)
            }

            if (this@BaseDialog !is BaseVMDialog<*, *>) {
                setContentView(getLayoutId())
            }
        }
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

    fun showMsg(msg: String) {
        baseActivity.showMsg(msg)
    }

    fun showError(throwable: Throwable) {
        baseActivity.handleError(throwable)
    }

    fun showMsg(msg: String, actionMsg: String, listener: View.OnClickListener) {
        baseActivity.showMsg(msg, actionMsg, listener)
    }

    fun showErrorMsg(msg: String, actionMsg: String, listener: View.OnClickListener) {
        baseActivity.showErrorMsg(msg, actionMsg, listener)
    }
}