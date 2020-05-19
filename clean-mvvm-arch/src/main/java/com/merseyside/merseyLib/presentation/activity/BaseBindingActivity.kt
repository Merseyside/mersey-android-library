package com.merseyside.merseyLib.presentation.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<B: ViewDataBinding> : BaseActivity() {

    protected lateinit var binding: B

    protected var isBindingInit = false

    override fun setView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this@BaseBindingActivity

        isBindingInit = true
    }

    override fun onDestroy() {
        super.onDestroy()

        isBindingInit = false
    }
}