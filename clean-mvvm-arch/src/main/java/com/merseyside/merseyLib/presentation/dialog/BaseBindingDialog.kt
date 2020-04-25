package com.merseyside.merseyLib.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingDialog<B: ViewDataBinding> : BaseDialog() {

    protected lateinit var binding: B

    protected var isBindingInit = false

    @CallSuper
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), null, false)
        binding.lifecycleOwner = this

        dialog.setContentView(binding.root)

        isBindingInit = true

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()

        isBindingInit = false
    }
}