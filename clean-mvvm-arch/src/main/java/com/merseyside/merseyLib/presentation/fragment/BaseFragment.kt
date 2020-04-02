package com.merseyside.merseyLib.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.merseyside.merseyLib.BaseApplication
import com.merseyside.merseyLib.presentation.activity.BaseActivity
import com.merseyside.merseyLib.presentation.activity.Orientation
import com.merseyside.merseyLib.presentation.view.IView
import com.merseyside.merseyLib.presentation.view.OnKeyboardStateListener
import com.merseyside.merseyLib.presentation.view.OrientationHandler
import com.merseyside.merseyLib.presentation.view.localeViews.ILocaleManager
import com.merseyside.merseyLib.utils.Logger
import com.merseyside.merseyLib.utils.SnackbarManager
import com.merseyside.merseyLib.utils.ext.isNotNullAndEmpty

abstract class BaseFragment : Fragment(), IView, OrientationHandler, ILocaleManager {

    final override var keyboardUnregistrar: Any? = null

    protected lateinit var baseActivity: BaseActivity
        private set

    private var requestCode: Int? = null
    private var fragmentResult: FragmentResult? = null

    private var currentLanguage: String = ""

    var snackbarManager: SnackbarManager? = null

    final override var orientation: Orientation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }

    override fun getContext(): Context {
        return baseActivity.getContext()
    }

    fun getLanguage(): String {
        return baseActivity.getLanguage()
    }

    protected abstract fun performInjection(bundle: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performInjection(savedInstanceState)
    }

    override fun onOrientationChanged(orientation: Orientation, savedInstanceState: Bundle?) {}

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RESULT_CODE_KEY)) {
                val resultCode = savedInstanceState.getInt(RESULT_CODE_KEY)
                this.requestCode = savedInstanceState.getInt(REQUEST_CODE_KEY)

                var bundle: Bundle? = null
                if (savedInstanceState.containsKey(RESULT_BUNDLE_KEY)) {
                    bundle = savedInstanceState.getBundle(RESULT_BUNDLE_KEY)
                }

                this.fragmentResult = FragmentResult(resultCode, requestCode!!, bundle)
            } else if (savedInstanceState.containsKey(REQUEST_CODE_KEY)) {
                this.requestCode = savedInstanceState.getInt(REQUEST_CODE_KEY)
            }
        }

        restoreLanguage(savedInstanceState)

        setOrientation(resources, savedInstanceState)

        snackbarManager = baseActivity.snackbarManager

        return inflater.inflate(getLayoutId(), container, false)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getToolbar()?.let {
            baseActivity.setFragmentToolbar(it)
        }

        baseActivity.getLanguage().run {
            if (currentLanguage.isNotNullAndEmpty() && this != currentLanguage) {
                updateLanguage(baseActivity.getContext())
            }

            currentLanguage = this
        }
    }

    override fun onStart() {
        super.onStart()

        setTitle()

        if (this is OnKeyboardStateListener) {
            keyboardUnregistrar = baseActivity.registerKeyboardListener(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (fragmentResult != null) {
            outState.putInt(RESULT_CODE_KEY, fragmentResult!!.resultCode)
            outState.putInt(REQUEST_CODE_KEY, fragmentResult!!.requestCode)
            if (fragmentResult!!.bundle != null) {
                outState.putBundle(RESULT_BUNDLE_KEY, fragmentResult!!.bundle)
            }
        } else if (requestCode != null) {
            outState.putInt(REQUEST_CODE_KEY, requestCode!!)
        }

        saveOrientation(outState)
        saveLanguage(outState)
    }

    override fun onStop() {
        super.onStop()

        if (snackbarManager?.isShowing() == true) {
            snackbarManager!!.dismiss()
        }

        unregisterKeyboardListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        getToolbar()?.let {
            baseActivity.setFragmentToolbar(null)
        }
    }

    fun hideKeyboard(view: View) {
        baseActivity.hideKeyboard(context, view)
    }

    override fun handleError(throwable: Throwable) {
        baseActivity.handleError(throwable)
    }

    override fun showMsg(msg: String, actionMsg: String?, clickListener: View.OnClickListener?) {

       snackbarManager?.apply {
           showSnackbar(
               message = msg,
               actionMsg = actionMsg,
               clickListener = clickListener
           )
       }
    }

    override fun showErrorMsg(msg: String, actionMsg: String?, clickListener: View.OnClickListener?) {

       snackbarManager?.apply {
           showErrorSnackbar(
               message = msg,
               actionMsg = actionMsg,
               clickListener = clickListener
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

    @CallSuper
    open fun updateLanguage(context: Context) {
        updateLocale(context = context)
    }

    override fun setLanguage(lang: String?) {
        baseActivity.setLanguage(lang)

        setTitle()
    }

    protected abstract fun getTitle(context: Context): String?

    fun setTitle(title: String? = null) {
        val context = if (baseActivity.applicationContext is BaseApplication) {
            (baseActivity.applicationContext as BaseApplication).context
        } else {
            baseActivity
        }

        val text = title ?: getTitle(context)

        if (text != null) {
            getActionBar()?.apply {
                this.title = text
            }
        }
    }

    protected open fun getActionBar(): ActionBar? {
        return baseActivity.supportActionBar
    }

    open fun getToolbar(): Toolbar? {
        return null
    }

    override fun showAlertDialog(
        title: String?,
        message: String?,
        positiveButtonText: String?,
        negativeButtonText: String?,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit,
        isOneAction: Boolean?,
        isCancelable: Boolean?) {
        
        baseActivity.showAlertDialog(
            title,
            message,
            positiveButtonText,
            negativeButtonText,
            onPositiveClick,
            onNegativeClick,
            isOneAction,
            isCancelable
        )
    }

    override fun showAlertDialog(
        @StringRes titleRes: Int?,
        @StringRes messageRes: Int?,
        @StringRes positiveButtonTextRes: Int?,
        @StringRes negativeButtonTextRes: Int?,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit,
        isOneAction: Boolean?,
        isCancelable: Boolean?
    ) {

        baseActivity.showAlertDialog(
            titleRes,
            messageRes,
            positiveButtonTextRes,
            negativeButtonTextRes,
            onPositiveClick,
            onNegativeClick,
            isOneAction,
            isCancelable
        )
    }

    override fun getActualString(@StringRes id: Int?, vararg args: String): String? {
        return baseActivity.getActualString(id, *args)
    }

    override fun onDetach() {
        super.onDetach()

        if (requestCode != null) {
            val result = if (fragmentResult == null) {
                FragmentResult(RESULT_CANCELLED, requestCode!!)
            } else {
                fragmentResult!!
            }

            baseActivity.setFragmentResult(result)
        }
    }

    protected fun setFragmentResult(resultCode: Int, bundle: Bundle? = null) {
        if (requestCode != null) {
            this.fragmentResult = FragmentResult(resultCode, requestCode!!, bundle)
        } else throw IllegalStateException("Firstly, set request code")
    }

    protected fun setRequestCode(requestCode: Int) {
        this.requestCode = requestCode
    }

    protected fun isStartedForResult(): Boolean {
        return requestCode != null
    }

    private fun saveLanguage(outState: Bundle) {
        outState.putString(LANGUAGE_KEY, currentLanguage)
    }

    private fun restoreLanguage(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(LANGUAGE_KEY)) {
            currentLanguage = savedInstanceState.getString(LANGUAGE_KEY, "")
        }
    }

    override fun getRootView(): View? {
        return view
    }

    open fun onFragmentResult(resultCode: Int, requestCode: Int, bundle: Bundle? = null) {}

    class FragmentResult(
        val resultCode: Int,
        val requestCode: Int,
        val bundle: Bundle? = null
    )

    companion object {
        const val RESULT_OK = -1
        const val RESULT_CANCELLED = 0

        private const val LANGUAGE_KEY = "language_mvvm_lib"

        private const val REQUEST_CODE_KEY = "requestCode"
        private const val RESULT_CODE_KEY = "resultCode"
        private const val RESULT_BUNDLE_KEY = "resultBundle"
    }
}
