package com.merseyside.archy.presentation.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

abstract class NavigationBaseFragment : Fragment() {

    protected val navController: NavController
        get() {
            return findNavController()
        }

    fun navigate(@IdRes fragmentId: Int) {
        navController.navigate(fragmentId)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

}