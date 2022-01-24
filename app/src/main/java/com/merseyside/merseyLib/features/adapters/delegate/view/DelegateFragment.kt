package com.merseyside.merseyLib.features.adapters.delegate.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.merseyside.adapters.ext.onItemClicked
import com.merseyside.merseyLib.BR
import com.merseyside.merseyLib.R
import com.merseyside.merseyLib.application.base.BaseSampleFragment
import com.merseyside.merseyLib.databinding.FragmentDelegateBinding
import com.merseyside.merseyLib.features.adapters.delegate.adapter.AnimalsAdapter
import com.merseyside.merseyLib.features.adapters.delegate.di.DaggerDelegateComponent
import com.merseyside.merseyLib.features.adapters.delegate.di.DelegateModule
import com.merseyside.merseyLib.features.adapters.delegate.entity.ButtonItem
import com.merseyside.merseyLib.features.adapters.delegate.entity.Cat
import com.merseyside.merseyLib.features.adapters.delegate.entity.Dog
import com.merseyside.merseyLib.features.adapters.delegate.model.DelegateViewModel

class DelegateFragment: BaseSampleFragment<FragmentDelegateBinding, DelegateViewModel>() {

    private val adapter = AnimalsAdapter().apply {
        onItemClicked { showMsg("Clicked!") }
        //delegatesManager.addDelegates(ButtonDelegateAdapter())
    }

    override fun hasTitleBackButton() = true
    override fun getLayoutId() = R.layout.fragment_delegate
    override fun getTitle(context: Context) = "Delegate"
    override fun getBindingVariable() = BR.viewModel

    override fun performInjection(bundle: Bundle?, vararg params: Any) {
        DaggerDelegateComponent.builder()
            .appComponent(appComponent)
            .delegateModule(getDelegateModule())
            .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireBinding().recycler.adapter = adapter
        adapter.add(listOf(
            Cat("Squirty", 5, "abc"),
            Cat("Mary", 12, "def"),
            ButtonItem("Button"),
            Dog("Woof", 1, "ghi"),
        ))
    }

    private fun getDelegateModule(): DelegateModule {
        return DelegateModule(this)
    }
}