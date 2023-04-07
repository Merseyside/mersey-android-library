package com.merseyside.utils.fragment.onBackPressedDispatcher

import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.merseyside.merseyLib.kotlin.extensions.isNotZero


/**
 * Create new on back pressed callback with enabled state equals true by default.
 * Remember if callback is enabled than we have to handle this action somehow and another callbacks won't be triggered.
 */
fun Fragment.setOnBackPressedCallback(
    enabled: Boolean = true,
    callback: OnBackPressedCallback.() -> Unit
): OnBackPressedCallback {
    return requireActivity()
        .onBackPressedDispatcher.addCallback(this, enabled, callback)
}

fun Fragment.onBackPressed() {
    requireActivity().onBackPressedDispatcher.onBackPressed()
}

/**
 * Created new on back pressed callback and enables/disables it by fragment's manager back stack changing.
 * Use it when your fragment has child fragments and needs to pop them before we navigate up from current fragment.
 */
fun Fragment.setOnBackPressedCallback(
    fragmentManager: FragmentManager,
    onBack: () -> Unit = { fragmentManager.popBackStackImmediate() }
): OnBackPressedCallback {
    val callback = setOnBackPressedCallback(enabled = false) { onBack() }
    fragmentManager.addOnBackStackChangedListener {
        callback.isEnabled = fragmentManager.backStackEntryCount.isNotZero()
    }

    return callback
}
