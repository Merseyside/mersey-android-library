package com.mereyside.merseyLib.presentation.activity

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class NavigationBaseActivity : AppCompatActivity() {

    protected val navController: NavController
        get() {
            val host = supportFragmentManager
                .findFragmentById(getFragmentContainer()) as NavHostFragment

            return host.navController
        }

    @IdRes
    abstract fun getFragmentContainer(): Int

    fun navigate(@IdRes fragmentId: Int) {
        navController.navigate(fragmentId)
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}