package com.merseyside.merseyLib.application.main.fragment.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.merseyside.archy.utils.ext.navigate
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.application.main.fragment.di.DaggerMainComponent
import com.merseyside.merseyLib.application.main.fragment.di.MainModule
import com.merseyside.merseyLib.application.main.fragment.model.MainViewModel
import com.merseyside.merseyLib.databinding.FragmentMainBinding
import com.merseyside.utils.view.ext.onClick

class MainFragment : BaseSampleFragment<FragmentMainBinding, MainViewModel>() {

    override fun getBindingVariable() = BR.viewModel
    override fun getLayoutId() = R.layout.fragment_main
    override fun getTitle(context: Context): String = "Choose a feature"

    override fun performInjection(bundle: Bundle?, vararg params: Any) {
        DaggerMainComponent.builder()
            .appComponent(appComponent)
            .mainModule(MainModule(this))
            .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireBinding().run {
            locationButton.onClick { navigate(R.id.action_mainFragment_to_locationFragment) }
        }
    }
}