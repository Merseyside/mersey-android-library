package com.merseyside.merseyLib.presentation.activity

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

abstract class NavigationBaseActivity : AppCompatActivity() {

    @IdRes
    open fun getFragmentContainer(): Int? {
        return null
    }
}