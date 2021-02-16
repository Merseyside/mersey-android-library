package com.merseyside.archy.presentation.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<B: ViewDataBinding> : BaseFragment() {

    private var binding: B? = null

    protected fun getBinding(): B {
        return binding ?: throw IllegalStateException("Binding is null. Do you call it after OnCreateView()?")
    }

    protected val isBindingInit: Boolean
        get() { return binding != null }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        @LayoutRes layoutId: Int
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        getBinding().lifecycleOwner = this

        return getBinding().root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}