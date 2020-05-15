package com.merseyside.merseyLib.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.merseyside.merseyLib.presentation.model.BaseViewModel
import javax.inject.Inject

abstract class BaseVMDialog<B : ViewDataBinding, M : BaseViewModel> : BaseBindingDialog<B>() {

    @Inject
    protected lateinit var viewModel: M

    private val errorObserver = Observer<Throwable> { this.handleError(it) }
    private val messageObserver = Observer<BaseViewModel.TextMessage?> { message ->
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
            errorLiveEvent.observe(baseActivity, errorObserver)
            messageLiveEvent.observe(baseActivity, messageObserver)
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