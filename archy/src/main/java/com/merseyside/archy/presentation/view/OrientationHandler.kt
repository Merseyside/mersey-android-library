package com.merseyside.archy.presentation.view

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import com.merseyside.utils.serialization.deserialize
import com.merseyside.utils.serialization.serialize
import com.merseyside.archy.presentation.activity.Orientation
import kotlinx.serialization.ImplicitReflectionSerializer

interface OrientationHandler {

    var orientation: Orientation?

    /**
     * Calls only if previous and current orientations are not the same
     */
    fun onOrientationChanged(orientation: Orientation, savedInstanceState: Bundle?) {}

    private fun getCurrentOrientation(resources: Resources): Orientation {
        return when(resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> Orientation.LANDSCAPE
            Configuration.ORIENTATION_PORTRAIT -> Orientation.PORTRAIT
            else -> Orientation.UNDEFINED
        }
    }

    fun setOrientation(resources: Resources, savedInstanceState: Bundle?) {
        restoreOrientation(savedInstanceState)

        getCurrentOrientation(resources).also { newOrientation ->
            if (orientation != null && orientation != newOrientation) {
                onOrientationChanged(newOrientation, savedInstanceState)
            }

            orientation = newOrientation
        }
    }

    @OptIn(ImplicitReflectionSerializer::class)
    fun saveOrientation(outState: Bundle) {
        if (orientation != null) {
            outState.putString(ORIENTATION_TAG, orientation!!.serialize())
        }
    }

    @OptIn(ImplicitReflectionSerializer::class)
    fun restoreOrientation(savedInstanceState: Bundle?) {
        orientation = savedInstanceState?.getString(ORIENTATION_TAG)?.deserialize()
    }

    companion object {
        private const val ORIENTATION_TAG = "orientation_mvvm_lib"
    }
}