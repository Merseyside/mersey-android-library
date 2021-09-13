package com.merseyside.merseyLib.application.main.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.merseyside.merseyLib.R
import com.merseyside.archy.presentation.activity.BaseBindingActivity
import com.merseyside.merseyLib.databinding.ActivityMainBinding
import com.merseyside.merseyLib.time.Millis
import com.merseyside.utils.delayedMainThread
import com.merseyside.utils.ext.getColorFromAttr
import com.merseyside.utils.ext.getResourceFromAttr
import com.merseyside.utils.ext.getStringFromAttr
import com.merseyside.utils.ext.log

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override fun performInjection(bundle: Bundle?) {}
    override fun getLayoutId() = R.layout.activity_main
    override fun getToolbar(): Toolbar = requireBinding().toolbar
    override fun getFragmentContainer() = R.id.nav_host_fragment

    override fun onResume() {
        super.onResume()
    }
}