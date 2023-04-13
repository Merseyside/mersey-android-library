package com.merseyside.utils.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * This method do the same as NavigationUI's method, except set the destination's label automatically.
 * Enables up button in the left top corner and hide it if it is a top level destination.
 * See @see[androidx.navigation.ui.Toolbar.Kt]
 */
fun Fragment.setupWithNavController(toolbar: Toolbar) {
    val navController = findNavController()
    val activity = requireActivity() as AppCompatActivity
    val actionBar = activity.supportActionBar

    actionBar?.let {
        if (!navController.isTopLevelDestination() || navController.previousBackStackEntry != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { navController.navigateUp() }
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false)
        }
    }
}