package com.merseyside.merseyLib.presentation.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<B: ViewDataBinding> : BaseFragment() {

    protected lateinit var binding: B

    protected var isBindingInit = false

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        @LayoutRes layoutId: Int
    ): View? {
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