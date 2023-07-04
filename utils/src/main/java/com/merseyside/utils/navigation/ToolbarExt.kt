package com.merseyside.utils.navigation

import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * This method do the same as NavigationUI's method, except set the destination's label automatically.
 * Enables up button in the left top corner and hide it if it is a top level destination.
 * See @see[androidx.navigation.ui.Toolbar.Kt]
 */
fun Fragment.setupWithNavController(toolbar: Toolbar, listener: OnClickListener) {
    val navController = findNavController()
    val activity = requireActivity() as AppCompatActivity
    val actionBar = activity.supportActionBar

    actionBar?.let {
        if (!navController.isTopLevelDestination() || navController.previousBackStackEntry != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener(listener)
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false)
        }
    }
}