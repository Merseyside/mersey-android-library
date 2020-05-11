package com.merseyside.merseyLib.presentation.application

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.presentation.activity.BaseActivity

class MainActivity : BaseActivity() {

    override fun performInjection(bundle: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getToolbar(): Toolbar? {
        return null
    }
}