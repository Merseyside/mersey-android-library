package com.merseyside.archy.presentation.dialog

import android.app.Dialog
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingDialog<B: ViewDataBinding> : BaseDialog() {

    private var binding: B? = null

    protected fun getBinding(): B {
        return binding ?: throw IllegalStateException("Binding is null. Do you call it after OnCreateView()?")
    }

    protected val isBindingInit: Boolean
        get() { return binding != null }

    override fun setView(dialog: Dialog, layoutId: Int) {
        binding = DataBindingUtil.inflate<B>(
            LayoutInflater.from(context),
            getLayoutId(),
            null,
            false
        ).apply {
            lifecycleOwner = this@BaseBindingDialog
        }.also {
            dialog.setContentView(it.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}