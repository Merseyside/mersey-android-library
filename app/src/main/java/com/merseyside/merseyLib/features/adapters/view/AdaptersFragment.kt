package com.merseyside.merseyLib.features.adapters.view

import android.content.Context
import android.os.Bundle
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentAdaptersBinding
import com.merseyside.merseyLib.features.adapters.di.AdaptersModule
import com.merseyside.merseyLib.features.adapters.di.DaggerAdaptersComponent
import com.merseyside.merseyLib.features.adapters.model.AdaptersViewModel

class AdaptersFragment : BaseSampleFragment<FragmentAdaptersBinding, AdaptersViewModel>() {

    override fun getBindingVariable() = BR.viewModel

    override fun performInjection(bundle: Bundle?) {
        DaggerAdaptersComponent.builder()
            .appComponent(appComponent)
            .adaptersModule(AdaptersModule(this))
            .build().inject(this)
    }

    override fun getLayoutId() = R.layout.fragment_adapters
    override fun getTitle(context: Context) = "kek"

}