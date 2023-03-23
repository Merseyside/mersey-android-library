package com.merseyside.archy.presentation.interfaces

import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment


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
