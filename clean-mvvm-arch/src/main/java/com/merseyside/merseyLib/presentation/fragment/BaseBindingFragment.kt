package com.merseyside.merseyLib.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<B: ViewDataBinding> : BaseFragment() {

    protected lateinit var binding: B

    protected var isBindingInit = false

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this

        isBindingInit = true

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        isBindingInit = false
    }
}