package com.merseyside.kmpMerseyLib.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.merseyside.kmpMerseyLib.presentation.model.BaseViewModel
import com.merseyside.archy.dialog.BaseBindingDialog
import javax.inject.Inject

abstract class BaseVMDialog<B : ViewDataBinding, M : BaseViewModel> : BaseBindingDialog<B>() {

    @Inject
    protected lateinit var viewModel: M

    private val errorObserver = { throwable: Throwable? ->
        if (throwable != null) {
            this.handleError(throwable)
        }
    }

    private val messageObserver = { message: BaseViewModel.TextMessage? ->
        if (message != null) {
            if (message.isError) {
                showErrorMsg(message)
            } else {
                showMsg(message)
            }

            viewModel.messageLiveEvent.value = null
        }
    }

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

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.apply {
            errorLiveEvent.addObserver(errorObserver)
            messageLiveEvent.addObserver(messageObserver)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun showMsg(textMessage: BaseViewModel.TextMessage) {
        if (textMessage.actionMsg.isNullOrEmpty()) {
            showMsg(textMessage.msg)
        } else {
            showMsg(textMessage.msg, null, textMessage.actionMsg!!, textMessage.onClick)
        }
    }

    private fun showErrorMsg(textMessage: BaseViewModel.TextMessage) {
        if (textMessage.actionMsg.isNullOrEmpty()) {
            showErrorMsg(textMessage.msg)
        } else {
            showErrorMsg(textMessage.msg, null, textMessage.actionMsg!!, textMessage.onClick)
        }
    }
}