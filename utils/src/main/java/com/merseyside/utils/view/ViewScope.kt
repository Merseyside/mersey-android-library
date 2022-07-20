package com.merseyside.utils.view

import android.view.View
import androidx.core.view.ViewCompat
import com.merseyside.merseyLib.kotlin.logger.logMsg
import com.merseyside.utils.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

private val KEY_VIEW_SCOPE = R.id.view_scope

val View.viewScope: CoroutineScope
    get() {
        getTag(KEY_VIEW_SCOPE)?.let {
            if (it is CoroutineScope)
                it
            else logMsg("check why the value of KEY_VIEW_SCOPE is ${it.javaClass.name}")
        }

        //////////////////////
        val scope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        setTag(KEY_VIEW_SCOPE, scope)

        //////////////////////
        if (!ViewCompat.isAttachedToWindow(this)) {
            logMsg("Creating a CoroutineScope before ${javaClass.name} attaches to a window. " +
                    "Coroutine jobs won't be canceled if the view has never been attached to a window.")
        }

        //////////////////////
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {}

            override fun onViewDetachedFromWindow(view: View) {
                removeOnAttachStateChangeListener(this)
                setTag(KEY_VIEW_SCOPE, null)
                scope.close()
            }
        })

        return scope
    }

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}