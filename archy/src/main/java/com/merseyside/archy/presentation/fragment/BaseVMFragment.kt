package com.merseyside.archy.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.merseyside.archy.model.BaseViewModel
import com.merseyside.archy.model.ParcelableViewModel
import com.merseyside.utils.PermissionManager
import javax.inject.Inject

abstract class BaseVMFragment<B : ViewDataBinding, M : BaseViewModel> : BaseBindingFragment<B>() {

    @Inject
    protected lateinit var viewModel: M

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

    private val errorObserver = Observer<Throwable> { this.handleError(it!!) }
    private val progressObserver = Observer<Boolean> { this.loadingObserver(it!!) }

    private val alertDialogModelObserver = Observer<BaseViewModel.AlertDialogModel> {
        it?.apply {
            showAlertDialog(title, message, positiveButtonText, negativeButtonText, onPositiveClick, onNegativeClick, isSingleAction, isCancelable)

            viewModel.alertDialogLiveEvent.value = null
        }
    }

    private val permissionObserver = Observer<Pair<Array<String>, Int>> { pair ->
        PermissionManager.requestPermissions(this, *pair.first, requestCode = pair.second)
    }

    abstract fun getBindingVariable(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            setVariable(getBindingVariable(), viewModel)
            executePendingBindings()
        }

        viewModel.apply {
            errorLiveEvent.observe(viewLifecycleOwner, errorObserver)
            messageLiveEvent.observe(viewLifecycleOwner, messageObserver)
            isInProgressLiveData.observe(viewLifecycleOwner, progressObserver)
            alertDialogLiveEvent.observe(viewLifecycleOwner, alertDialogModelObserver)
            grantPermissionLiveEvent.observe(viewLifecycleOwner, permissionObserver)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (viewModel is ParcelableViewModel) {
            (viewModel as ParcelableViewModel).writeTo(outState)
        }
    }

    override fun updateLanguage(context: Context) {
        super.updateLanguage(context)
        viewModel.updateLanguage(context)
    }

    protected open fun loadingObserver(isLoading: Boolean) {}

    private fun showErrorMsg(textMessage: BaseViewModel.TextMessage) {
        if (textMessage.actionMsg.isNullOrEmpty()) {
            showErrorMsg(textMessage.msg)
        } else {
            showErrorMsg(textMessage.msg, null, textMessage.actionMsg!!, textMessage.onClick)
        }
    }

    private fun showMsg(textMessage: BaseViewModel.TextMessage) {
        if (textMessage.actionMsg.isNullOrEmpty()) {
            showMsg(textMessage.msg)
        } else {
            showMsg(textMessage.msg, null, textMessage.actionMsg!!, textMessage.onClick)
        }
    }

    protected fun showProgress() {
        viewModel.showProgress()
    }

    protected fun hideProgress() {
        viewModel.hideProgress()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.apply {
            errorLiveEvent.removeObserver(errorObserver)
            messageLiveEvent.removeObserver(messageObserver)
            isInProgressLiveData.removeObserver(progressObserver)
            alertDialogLiveEvent.removeObserver(alertDialogModelObserver)
            grantPermissionLiveEvent.removeObserver(permissionObserver)
        }
    }
}
