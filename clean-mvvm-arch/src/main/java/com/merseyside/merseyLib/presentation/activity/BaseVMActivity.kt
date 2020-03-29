package com.merseyside.merseyLib.presentation.activity

import android.content.Context

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.merseyside.merseyLib.presentation.model.BaseViewModel
import com.merseyside.merseyLib.presentation.model.ParcelableViewModel
import javax.inject.Inject

abstract class BaseVMActivity<B : ViewDataBinding, M : BaseViewModel> : BaseBindingActivity<B>() {

    @Inject
    protected lateinit var viewModel: M

    private val messageObserver = Observer<BaseViewModel.TextMessage> { message ->
        if (message.isError) {
            showErrorMsg(message)
        } else {
            showMsg(message)
        }

        viewModel.messageLiveEvent.value = null
    }

    private val loadingObserver = Observer<Boolean> { this.loadingObserver(it) }
    private val alertDialogModel = Observer<BaseViewModel.AlertDialogModel> {
        it.apply {
            showAlertDialog(title, message, positiveButtonText, negativeButtonText, onPositiveClick, onNegativeClick, isCancelable)
            
            viewModel.alertDialogLiveEvent.value = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBindingVariable()

        viewModel.updateLanguage(this)

        observeViewModel()
    }

    abstract fun getBindingVariable(): Int

    private fun setBindingVariable() {
        binding.apply {
            setVariable(getBindingVariable(), viewModel)
            executePendingBindings()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (viewModel is ParcelableViewModel) {
            (viewModel as ParcelableViewModel).writeTo(outState)
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            messageLiveEvent.observe(this@BaseVMActivity, messageObserver)
            isInProgressLiveData.observe(this@BaseVMActivity, loadingObserver)
            alertDialogLiveEvent.observe(this@BaseVMActivity, alertDialogModel)
        }
    }

    override fun handleError(throwable: Throwable) {
        viewModel.onError(throwable)
    }

    override fun updateLanguage(context: Context) {
        viewModel.updateLanguage(context)
    }

    protected abstract fun loadingObserver(isLoading: Boolean)


    protected fun showErrorMsg(textMessage: BaseViewModel.TextMessage) {
        if (textMessage.actionMsg.isNullOrEmpty()) {
            showErrorMsg(textMessage.msg)
        } else {
            showErrorMsg(textMessage.msg, textMessage.actionMsg!!, textMessage.listener!!)
        }
    }

    protected fun showMsg(textMessage: BaseViewModel.TextMessage) {
        if (textMessage.actionMsg.isNullOrEmpty()) {
            showMsg(textMessage.msg)
        } else {
            showMsg(textMessage.msg, textMessage.actionMsg!!, textMessage.listener!!)
        }
    }
}