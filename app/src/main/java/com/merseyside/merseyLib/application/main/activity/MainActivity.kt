package com.merseyside.merseyLib.application.main.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.merseyside.merseyLib.R
import com.merseyside.archy.presentation.activity.BaseActivity

class MainActivity : BaseActivity() {

    override fun performInjection(bundle: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getToolbar(): Toolbar? {
        return null
    }

    override fun getFragmentContainer(): Int {
        TODO("Not yet implemented")
    }
}