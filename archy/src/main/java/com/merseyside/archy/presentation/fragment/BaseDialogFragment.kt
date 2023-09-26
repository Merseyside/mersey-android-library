package com.merseyside.archy.presentation.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.merseyside.archy.BaseApplication
import com.merseyside.archy.presentation.activity.BaseActivity
import com.merseyside.archy.presentation.activity.Orientation
import com.merseyside.archy.presentation.view.IView
import com.merseyside.archy.presentation.view.OnKeyboardStateListener
import com.merseyside.archy.presentation.view.OrientationHandler
import com.merseyside.archy.presentation.view.localeViews.ILocaleManager
import com.merseyside.archy.utils.SnackbarManager
import com.merseyside.merseyLib.kotlin.extensions.isNotNullAndEmpty
import com.merseyside.utils.delegate.FragmentArgumentHelper
import com.merseyside.utils.delegate.argumentHelper
import com.merseyside.utils.delegate.bool

abstract class BaseDialogFragment : DialogFragment(), IView, OrientationHandler, ILocaleManager {

    val argsHelper: FragmentArgumentHelper by argumentHelper()
    open var isBottomDialog: Boolean by argsHelper.bool(false)
    open var isDialog: Boolean by argsHelper.bool(false)

    final override var keyboardUnregistrar: Any? = null

    lateinit var baseActivity: BaseActivity
        private set

    private var currentLanguage: String = ""

    protected var snackbarManager: SnackbarManager? = null
    protected lateinit var menuHost: MenuHost
        private set

    final override var orientation: Orientation? = null

    private var fragmentOnBackPressedCallback: OnBackPressedCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }


    open var isBarVisible: Boolean = true

    fun setBarVisibility(isVisible: Boolean) {
        if (isVisible != isBarVisible) {
            isBarVisible = isVisible
            baseActivity.setBarVisibility(isVisible)
        }
    }

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

    /**
     * First of all perform injection, because when state restores, it calls viewModels which
     * requires all dependencies and state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        performInjection(savedInstanceState)
        super.onCreate(savedInstanceState)
        showsDialog = isDialog
    }

    /**
     * may be STYLE_NORMAL, STYLE_NO_TITLE, STYLE_NO_FRAME, or STYLE_NO_INPUT
     */
    open fun getDialogStyle(): Int {
        return STYLE_NORMAL
    }

    open fun getDialogTheme(): Int {
        return 0 // will be selected corresponding to app style
    }

    open fun setDialogSize(dialog: Dialog) {
        throw NotImplementedError()
    }

    open fun isDialogCancellable(): Boolean = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = if (isBottomDialog) BottomSheetDialog(baseActivity, getDialogTheme())
        else Dialog(baseActivity, getDialogTheme())

        onDialogCreated(dialog, savedInstanceState)

        return dialog
    }

    protected open fun onDialogCreated(dialog: Dialog, savedInstanceState: Bundle?) {
        if (!isBottomDialog) setDialogSize(dialog)

        isCancelable = isDialogCancellable()
        val title = getTitle(context)

        if (title.isNullOrEmpty()) dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        else dialog.setTitle(title)

        dialog.setCanceledOnTouchOutside(isDialogCancellable())
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            restoreLanguage(savedInstanceState)
        }

        setOrientation(resources, savedInstanceState)
        snackbarManager = baseActivity.snackbarManager

        return inflateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isBottomDialog) setDialogSize(dialog ?: throw NullPointerException("Dialog is null!"))
        menuHost = requireActivity()
        setupAppBar()

        baseActivity.getLanguage().run {
            if (currentLanguage.isNotNullAndEmpty() && this != currentLanguage) {
                updateLanguage(baseActivity.getContext())
            }
            currentLanguage = this
        }

        setTitle()

        if (this is OnKeyboardStateListener) {
            keyboardUnregistrar = baseActivity.registerKeyboardListener(this)
        }
    }

    protected open fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        @LayoutRes layoutId: Int = getLayoutId()
    ) = inflater.inflate(layoutId, container, false)

    open fun getToolbar(): Toolbar? {
        return null
    }

    /**
     * Calls on view created.
     * If you have an inner fragment or you don't want to make fragment change toolbar at all
     * then override this method with empty implementation.
     *
     * Skip when showing as dialog.
     */
    open fun setupAppBar() {
        if (isDialog) return

        baseActivity.setFragmentToolbar(getToolbar())

        with(baseActivity) {
            setBarVisibility(isBarVisible)
            val isUpEnabled = isNavigateUpEnabled()
            supportActionBar?.setDisplayHomeAsUpEnabled(isUpEnabled)
            if (isUpEnabled) {
                getToolbar()?.setNavigationOnClickListener { this@BaseDialogFragment.onNavigateUp() }
            }
        }
    }

    abstract fun isNavigateUpEnabled(): Boolean

    abstract fun onNavigateUp()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveOrientation(outState)
        saveLanguage(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissMsg()
        unregisterKeyboardListener()
    }

    override fun handleError(throwable: Throwable): Boolean {
        return baseActivity.handleError(throwable)
    }

    override fun showMsg(msg: String, view: View?, actionMsg: String?, onClick: (() -> Unit)?) {
        snackbarManager?.apply {
            showSnackbar(
                view = view,
                message = msg,
                actionMsg = actionMsg,
                onClick = onClick
            )
        }
    }

    override fun showErrorMsg(
        msg: String,
        view: View?,
        actionMsg: String?,
        onClick: (() -> Unit)?
    ) {
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

    protected open fun getTitle(context: Context): String? = null

    fun setTitle(title: String? = null) {
        val context = (baseActivity.applicationContext as? BaseApplication)?.context ?: baseActivity

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
        isCancelable: Boolean?
    ) {
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

    private fun saveLanguage(outState: Bundle) {
        outState.putString(LANGUAGE_KEY, currentLanguage)
    }

    private fun restoreLanguage(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(LANGUAGE_KEY)) {
            currentLanguage = savedInstanceState.getString(LANGUAGE_KEY, "")
        }
    }

    override fun getRootView(): View? = view

    companion object {
        private const val LANGUAGE_KEY = "language_mvvm_lib"
    }
}

fun <T : BaseDialogFragment> T.asDialog(): T {
    isDialog = true
    return this
}

fun <T : BaseDialogFragment> T.asBottomDialog(): T {
    isDialog = true
    isBottomDialog = true
    return this
}