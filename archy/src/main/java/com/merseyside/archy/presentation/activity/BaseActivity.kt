package com.merseyside.archy.presentation.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.merseyside.archy.BaseApplication
import com.merseyside.archy.presentation.dialog.MaterialAlertDialog
import com.merseyside.archy.presentation.ext.getActualString
import com.merseyside.archy.presentation.fragment.BaseFragment
import com.merseyside.archy.presentation.view.OnKeyboardStateListener
import com.merseyside.archy.presentation.view.OrientationHandler
import com.merseyside.archy.presentation.view.localeViews.ILocaleManager
import com.merseyside.archy.utils.SnackbarManager
import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.utils.LocaleManager
import com.merseyside.utils.ext.getLocalizedContext
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

abstract class BaseActivity : AppCompatActivity(),
    IActivityView, OrientationHandler, ILocaleManager {

    override var keyboardUnregistrar: Any? = null

    final override var orientation: Orientation? = null

    private var application: BaseApplication? = null
    private lateinit var mainContext: Context

    override fun getContext(): Context {
        return mainContext
    }

    lateinit var snackbarManager: SnackbarManager

    override fun onOrientationChanged(orientation: Orientation, savedInstanceState: Bundle?) {}

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            val localeManager = LocaleManager(newBase)

            super.attachBaseContext(localeManager.getLocalizedContext()
                .also { mainContext = it })
        }
    }

    protected abstract fun performInjection(bundle: Bundle?, vararg params: Any)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (applicationContext is BaseApplication) {
            application = applicationContext as BaseApplication
        }

        setOrientation(resources, savedInstanceState)
        performInjection(savedInstanceState)
        setView()
        setSupportActionBar(activityToolbar)

        snackbarManager = SnackbarManager(this)
    }

    open fun setView(@LayoutRes layoutId: Int = getLayoutId()) {
        setContentView(layoutId)
    }

    override fun onStart() {
        super.onStart()

        if (this is OnKeyboardStateListener) {
            keyboardUnregistrar = registerKeyboardListener(this)
        }
    }

    override fun onStop() {
        super.onStop()
        dismissMsg()
        unregisterKeyboardListener()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveOrientation(outState)
    }

    open fun updateLanguage(context: Context) {}

    override fun setLanguage(lang: String?) {
        if (application != null) {
            val language = lang ?: getLanguage()
            mainContext = application!!.setLanguage(language)

            getCurrentFragment()?.updateLanguage(mainContext)
            updateLanguage(mainContext).also { updateLocale(context = mainContext) }
        }
    }

    override fun getLanguage(): String {
        return application?.getLanguage()
            ?: throw IllegalStateException("Please, extend your application from BaseApplication class")
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun showMsg(
        msg: String,
        view: View?,
        actionMsg: String?,
        onClick: (() -> Unit)?
    ) {
        snackbarManager.apply {
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
        snackbarManager.apply {
            showErrorSnackbar(
                view = view,
                message = msg,
                actionMsg = actionMsg,
                onClick = onClick
            )
        }
    }

    override fun handleError(throwable: Throwable): Boolean = false

//    override fun onSupportNavigateUp(): Boolean {
//        return safeLet(navController) { nav ->
//            nav.navigateUp()
//        } ?: super.onSupportNavigateUp()
//    }

    @IdRes
    abstract fun getFragmentContainer(): Int?

    fun getCurrentFragment(res: Int? = getFragmentContainer()) = res?.let {
        with(supportFragmentManager.findFragmentById(res)) {
            when (this) {
                is BaseFragment -> this
                is NavHostFragment -> this.getChildFragmentManager().fragments[0] as BaseFragment
                else -> null
            }
        }
    }

    val navHostFragment: NavHostFragment?
        get() = getFragmentContainer()?.let { id ->
            supportFragmentManager.findFragmentById(id) as NavHostFragment
        }

    val navController: NavController
        get() = navHostFragment?.navController ?:
            throw IllegalArgumentException("NavHostFragment is null! Are you sure you pass fragment" +
                    " container correctly?")


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
        val dialog = MaterialAlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButtonText(positiveButtonText)
            .setNegativeButtonText(negativeButtonText)
            .setOnPositiveClick(onPositiveClick)
            .setOnNegativeClick(onNegativeClick)
            .isSingleAction(isSingleAction)
            .isCancelable(isCancelable)
            .build()

        dialog.show()
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

        showAlertDialog(
            getActualString(titleRes),
            getActualString(messageRes),
            getActualString(positiveButtonTextRes),
            getActualString(negativeButtonTextRes),
            onPositiveClick,
            onNegativeClick,
            isSingleAction,
            isCancelable
        )
    }

    val activityToolbar: Toolbar? by lazy {
        getMainToolbar()
    }

    protected abstract fun getMainToolbar(): Toolbar?

    private var toolbar: Toolbar? = null

    fun getToolbar(): Toolbar? {
        return toolbar ?: getMainToolbar()
    }

    override fun setFragmentToolbar(fragmentToolbar: Toolbar?) {
        val possibleToolbar = fragmentToolbar ?: activityToolbar
        if (possibleToolbar != toolbar) {
            toolbar = possibleToolbar
            setSupportActionBar(possibleToolbar)

            val isFragmentBar = fragmentToolbar != null
            getMainToolbar()?.isGone = isFragmentBar
        }
    }

    override fun setBarVisibility(isVisible: Boolean) {
        supportActionBar?.apply {
            if (isVisible) show()
            else hide()
        }
    }

    override fun getActualString(@StringRes id: Int?, vararg args: String): String? {
        return applicationContext.getActualString(id, *args)
    }

    override fun setFragmentResult(fragmentResult: BaseFragment.FragmentResult) {
        fragmentResult.let {
            getCurrentFragment()?.onFragmentResult(it.resultCode, it.requestCode, it.bundle)
        }
    }

    override fun registerKeyboardListener(listener: OnKeyboardStateListener): Unregistrar {
        return KeyboardVisibilityEvent.registerEventListener(
            this
        ) { isVisible ->
            if (isVisible) listener.onKeyboardShown()
            else listener.onKeyboardHid()
        }
    }

    override fun dismissMsg() {
        if (snackbarManager.isShowing()) {
            snackbarManager.dismiss()
        } else {
            Logger.log(this, "Snackbar had not shown")
        }
    }

    override fun getRootView(): View? {
        return findViewById<View>(android.R.id.content).rootView
    }
}
