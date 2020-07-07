package com.merseyside.archy.presentation.dialog

import android.app.Dialog
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingDialog<B: ViewDataBinding> : BaseDialog() {

    protected lateinit var binding: B

    protected var isBindingInit = false

    override fun setView(dialog: Dialog, layoutId: Int) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), null, false)
        binding.lifecycleOwner = this

        dialog.setContentView(binding.root)

        isBindingInit = true
    }

    override fun onDestroyView() {
        super.onDestroyView()

        isBindingInit = false
    }
}