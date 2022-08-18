package com.merseyside.archy.presentation.fragment

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
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import com.merseyside.archy.BaseApplication
import com.merseyside.archy.presentation.activity.BaseActivity
import com.merseyside.archy.presentation.activity.Orientation
import com.merseyside.archy.presentation.view.IView
import com.merseyside.archy.presentation.view.OnKeyboardStateListener
import com.merseyside.archy.presentation.view.OrientationHandler
import com.merseyside.archy.presentation.view.localeViews.ILocaleManager
import com.merseyside.archy.utils.SnackbarManager
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty

abstract class BaseFragment : Fragment(), IView, OrientationHandler, ILocaleManager {

    final override var keyboardUnregistrar: Any? = null

    protected lateinit var baseActivity: BaseActivity
        private set

    private var requestCode: Int = NO_REQUEST
    private var fragmentResult: FragmentResult? = null

    private var currentLanguage: String = ""

    protected var snackbarManager: SnackbarManager? = null
    protected lateinit var menuHost: MenuHost
        private set

    final override var orientation: Orientation? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }

    open fun isBarVisible(): Boolean = true

    override fun getContext(): Context {
        return baseActivity.getContext()
    }

    fun getLanguage(): String {
        return baseActivity.getLanguage()
    }

    override fun setLanguage(lang: String?) {
        baseActivity.setLanguage(lang)
        setTitle()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    protected abstract fun performInjection(bundle: Bundle?, vararg params: Any)

    override fun onCreate(savedInstanceState: Bundle?) {
        performInjection(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                if (savedInstanceState.containsKey(RESULT_CODE_KEY)) {

                    val bundle = if (containsKey(RESULT_BUNDLE_KEY)) {
                        getBundle(RESULT_BUNDLE_KEY)
                    } else null

                    setOnResultFragmentCodes(
                        requestCode = getInt(REQUEST_CODE_KEY),
                        resultCode = getInt(RESULT_CODE_KEY),
                        bundle = bundle
                    )

                } else if (containsKey(REQUEST_CODE_KEY)) {
                    requestCode = savedInstanceState.getInt(REQUEST_CODE_KEY)
                }
            }

            restoreLanguage(savedInstanceState)
        }

        setOrientation(resources, savedInstanceState)
        snackbarManager = baseActivity.snackbarManager

        return inflateView(inflater, container)
    }

    protected fun setOnResultFragmentCodes(
        requestCode: Int,
        resultCode: Int,
        bundle: Bundle? = null
    ) {
        setRequestCode(requestCode)
        this.fragmentResult = FragmentResult(resultCode, requestCode, bundle)
    }
    
    protected open fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        @LayoutRes layoutId: Int = getLayoutId()
    ) = inflater.inflate(layoutId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuHost = requireActivity()
        setupAppBar()

        baseActivity.getLanguage().run {
            if (currentLanguage.isNotNullAndEmpty() && this != currentLanguage) {
                updateLanguage(baseActivity.getContext())
            }
            currentLanguage = this
        }
    }

    private fun setupAppBar() {
        notifyToolbarChanged()
    }

    protected fun notifyToolbarChanged() {
        baseActivity.setFragmentToolbar(getToolbar(), isBarVisible())
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
        fragmentResult?.let { result ->
            outState.putInt(RESULT_CODE_KEY, result.resultCode)
            outState.putInt(REQUEST_CODE_KEY, result.requestCode)
            if (result.bundle != null) {
                outState.putBundle(RESULT_BUNDLE_KEY, result.bundle)
            }
        } ?: outState.putInt(REQUEST_CODE_KEY, requestCode)

        saveOrientation(outState)
        saveLanguage(outState)
    }

    override fun onStop() {
        super.onStop()
        dismissMsg()
        unregisterKeyboardListener()
    }

    override fun handleError(throwable: Throwable): Boolean {
        return baseActivity.handleError(throwable)
    }

    override fun showMsg(msg: String, view: View?, actionMsg: String?, onClick: () -> Unit) {
       snackbarManager?.apply {
           showSnackbar(
               view = view,
               message = msg,
               actionMsg = actionMsg,
               onClick = onClick
           )
       }
    }

    override fun showErrorMsg(msg: String, view: View?, actionMsg: String?, onClick: () -> Unit) {
       snackbarManager?.apply {
           showErrorSnackbar(
               view = view,
               message = msg,
               actionMsg = actionMsg,
               onClick = onClick
           )
       }
    }

    override fun dismissMsg() {
        if (snackbarManager?.isShowing() == true) {
            snackbarManager!!.dismiss()
        }
    }

    @CallSuper
    open fun updateLanguage(context: Context) {
        updateLocale(context = context)
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

    open fun getToolbar(): Toolbar? = null

    override fun showAlertDialog(
        title: String?,
        message: String?,
        positiveButtonText: String?,
        negativeButtonText: String?,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit,
        isSingleAction: Boolean?,
        isCancelable: Boolean?) {
        baseActivity.showAlertDialog(
            title,
            message,
            positiveButtonText,
            negativeButtonText,
            onPositiveClick,
            onNegativeClick,
            isSingleAction,
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
        isSingleAction: Boolean?,
        isCancelable: Boolean?) {
        baseActivity.showAlertDialog(
            titleRes,
            messageRes,
            positiveButtonTextRes,
            negativeButtonTextRes,
            onPositiveClick,
            onNegativeClick,
            isSingleAction,
            isCancelable
        )
    }

    override fun getActualString(@StringRes id: Int?, vararg args: String): String? {
        return baseActivity.getActualString(id, *args)
    }

    override fun onDetach() {
        super.onDetach()
        if (requestCode != NO_REQUEST) {
            val result = if (fragmentResult == null) {
                FragmentResult(RESULT_CANCELLED, requestCode)
            } else {
                fragmentResult!!
            }

            baseActivity.setFragmentResult(result)
        }
    }

    protected fun setFragmentResult(resultCode: Int, bundle: Bundle? = null) {
        if (requestCode != NO_REQUEST) {
            this.fragmentResult = FragmentResult(resultCode, requestCode, bundle)
        } else throw IllegalStateException("Firstly, set request code")
    }

    protected fun setRequestCode(requestCode: Int) {
        this.requestCode = requestCode
    }

    protected fun isStartedForResult(): Boolean {
        return requestCode != NO_REQUEST
    }

    private fun saveLanguage(outState: Bundle) {
        outState.putString(LANGUAGE_KEY, currentLanguage)
    }

    private fun restoreLanguage(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(LANGUAGE_KEY)) {
            currentLanguage = savedInstanceState.getString(LANGUAGE_KEY, "")
        }
    }

    override fun getRootView(): View? {
        return view
    }

    open fun goBack() {
        baseActivity.goBack()
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

        const val NO_REQUEST = -1

        private const val LANGUAGE_KEY = "language_mvvm_lib"

        private const val REQUEST_CODE_KEY = "requestCode"
        private const val RESULT_CODE_KEY = "resultCode"
        private const val RESULT_BUNDLE_KEY = "resultBundle"
    }
}
