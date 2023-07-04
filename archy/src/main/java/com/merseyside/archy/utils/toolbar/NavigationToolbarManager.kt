package com.merseyside.archy.utils.toolbar

import androidx.navigation.NavController
import com.merseyside.utils.navigation.isTopLevelDestination

interface NavigationToolbarManager : ToolbarManager {

    val navController: NavController

    override fun isNavigateUpEnabled(): Boolean {
        return !navController.isTopLevelDestination() || navController.previousBackStackEntry != null
    }

    override fun onNavigateUpClicked() {
        navController.navigateUp()
    }
}