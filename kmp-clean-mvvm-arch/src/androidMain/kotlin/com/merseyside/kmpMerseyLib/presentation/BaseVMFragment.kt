package com.merseyside.kmpMerseyLib.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.merseyside.kmpMerseyLib.presentation.model.BaseViewModel
import com.merseyside.kmpMerseyLib.presentation.model.ParcelableViewModel
import com.merseyside.archy.presentation.fragment.BaseBindingFragment
import com.merseyside.utils.PermissionManager
import com.merseyside.utils.serialization.putSerialize
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import javax.inject.Inject

abstract class BaseVMFragment<B : ViewDataBinding, M : BaseViewModel> : BaseBindingFragment<B>() {

    @Inject
    protected lateinit var viewModel: M

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

    private val errorObserver = { throwable: Throwable? ->
        if (throwable != null) {
            this.handleError(throwable)
        }
    }

    private val progressObserver = { isLoading: Boolean ->
        this.loadingObserver(isLoading)
    }

    private val alertDialogModelObserver = { model: BaseViewModel.AlertDialogModel? ->
        model?.apply {
            showAlertDialog(title, message, positiveButtonText, negativeButtonText, onPositiveClick, onNegativeClick, isSingleAction, isCancelable)

            viewModel.alertDialogLiveEvent.value = null
        }

        Unit
    }

    private val permissionObserver = { pair: Pair<Array<String>, Int>? ->
        if (pair != null) {
            PermissionManager.requestPermissions(this, *pair.first, requestCode = pair.second)
        }
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
            errorLiveEvent.addObserver(errorObserver)
            messageLiveEvent.addObserver(messageObserver)
            isInProgressLiveData.addObserver(progressObserver)
            alertDialogLiveEvent.addObserver(alertDialogModelObserver)
            grantPermissionLiveEvent.addObserver(permissionObserver)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (viewModel is ParcelableViewModel) {
            val bundle = com.merseyside.kmpMerseyLib.utils.Bundle()

            (viewModel as ParcelableViewModel).writeTo(bundle)
            outState.putSerialize(INSTANCE_STATE_KEY, bundle.map, MapSerializer(String.serializer(), String.serializer()))
        }
    }

    override fun updateLanguage(context: Context) {
        super.updateLanguage(context)
        //viewModel.updateLanguage(context)
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

    companion object {
        const val INSTANCE_STATE_KEY = "instance_state"
    }
}
