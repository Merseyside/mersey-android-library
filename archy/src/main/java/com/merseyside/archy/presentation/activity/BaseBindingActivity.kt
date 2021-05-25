package com.merseyside.archy.presentation.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<B : ViewDataBinding> : BaseActivity() {

    protected var binding: B? = null
        private set

    protected fun requireBinding(): B {
        return binding ?: throw IllegalStateException(
            "Binding is null. Do you call it after OnCreateView()?" +
                    " Current state is ${lifecycle.currentState}"
        )
    }

    protected val isBindingInit: Boolean
        get() {
            return binding != null
        }

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