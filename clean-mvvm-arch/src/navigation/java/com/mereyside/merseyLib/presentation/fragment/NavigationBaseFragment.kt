package com.mereyside.merseyLib.presentation.fragment

import androidx.annotation.IdRes
import androidx.navigation.fragment.NavHostFragment

abstract class NavigationBaseFragment : NavHostFragment() {

    fun navigate(@IdRes fragmentId: Int) {
        navController.navigate(fragmentId)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

}