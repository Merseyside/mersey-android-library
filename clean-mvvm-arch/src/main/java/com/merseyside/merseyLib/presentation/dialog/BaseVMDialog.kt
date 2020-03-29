package com.merseyside.merseyLib.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.merseyside.merseyLib.presentation.model.BaseViewModel
import javax.inject.Inject

abstract class BaseVMDialog<B : ViewDataBinding, M : BaseViewModel> : BaseBindingDialog<B>() {

    @Inject
    protected lateinit var viewModel: M

    private val errorObserver = Observer<Throwable> { this.showError(it) }
    private val messageObserver = Observer<BaseViewModel.TextMessage> { this.showMsg(it) }

    abstract fun getBindingVariable(): Int

    override fun onCreate(onSavedInstanceState: Bundle?) {
        performInjection(onSavedInstanceState)
        super.onCreate(onSavedInstanceState)

        setHasOptionsMenu(false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        binding.apply {
            setVariable(getBindingVariable(), viewModel)
            executePendingBindings()
        }

        viewModel.apply {
            updateLanguage(context!!)

            errorLiveEvent.observe(viewLifecycleOwner, errorObserver)
            messageLiveEvent.observe(viewLifecycleOwner, messageObserver)
        }

        return dialog
    }

    private fun showMsg(textMessage: BaseViewModel.TextMessage) {
        if (textMessage.actionMsg.isNullOrEmpty()) {
            showMsg(textMessage.msg)
        } else {
            showMsg(textMessage.msg, textMessage.actionMsg!!, textMessage.listener!!)
        }
    }
}