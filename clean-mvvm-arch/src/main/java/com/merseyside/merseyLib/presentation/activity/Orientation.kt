package com.merseyside.merseyLib.presentation.activity

import android.app.Activity
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
    return this.getActivity().getOrientation()
}

fun Activity.getOrientation(): Orientation {
    return if (this is BaseActivity) {
        this.orientation!!
    } else {
        throw IllegalStateException("Your activity has to extend BaseActivity")
    }
}