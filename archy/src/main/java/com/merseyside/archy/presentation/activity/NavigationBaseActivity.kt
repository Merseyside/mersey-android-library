package com.merseyside.archy.presentation.activity

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController

abstract class NavigationBaseActivity : AppCompatActivity() {

    protected val navController: NavController?
        get() {
            val container = getFragmentContainer()
            return if (container != null) {
                findNavController(container)
            } else {
                null
            }
        }

    @IdRes
    abstract fun getFragmentContainer(): Int?

    fun navigate(@IdRes fragmentId: Int) {
        navController?.navigate(fragmentId)
    }

    fun navigateUp() {
        navController?.navigateUp()
    }
}