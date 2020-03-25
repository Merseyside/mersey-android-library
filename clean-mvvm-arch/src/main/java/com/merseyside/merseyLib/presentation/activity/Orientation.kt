package com.merseyside.merseyLib.presentation.activity

import android.view.View
import com.merseyside.merseyLib.utils.ext.getActivity
import kotlinx.serialization.Serializable

@Serializable
enum class Orientation {
    PORTRAIT,
    LANDSCAPE,
    UNDEFINED
}

fun View.getOrientation(): Orientation {
    val activity = this.getActivity()

    return if (activity is BaseActivity) {
        activity.orientation!!
    } else {
        throw IllegalStateException("Your activity has to extend BaseActivity")
    }
}