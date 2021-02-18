package com.merseyside.archy.presentation.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<B: ViewDataBinding> : BaseActivity() {

    private var binding: B? = null

    protected fun getBinding(): B {
        return binding ?: throw IllegalStateException("Binding is null. Do you call it after OnCreateView()?")
    }

    protected val isBindingInit: Boolean
        get() { return binding != null }

    override fun setView(layoutId: Int) {
        binding = DataBindingUtil.setContentView<B>(this, getLayoutId()).apply {
            lifecycleOwner = this@BaseBindingActivity
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}