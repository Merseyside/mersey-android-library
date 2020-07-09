package com.merseyside.kmpMerseyLib.presentation

import android.content.Context
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.merseyside.kmpMerseyLib.presentation.model.BaseViewModel
import com.merseyside.kmpMerseyLib.presentation.model.ParcelableViewModel
import com.merseyside.archy.presentation.activity.BaseBindingActivity
import com.merseyside.utils.PermissionManager
import com.merseyside.utils.serialization.putSerialize
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import javax.inject.Inject

abstract class BaseVMActivity<B : ViewDataBinding, M : BaseViewModel> : BaseBindingActivity<B>() {

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

    private val loadingObserver = { isLoading: Boolean -> this.loadingObserver(isLoading) }
    private val alertDialogModel = { model: BaseViewModel.AlertDialogModel? ->
        model?.apply {
            showAlertDialog(title, message, positiveButtonText, negativeButtonText, onPositiveClick, onNegativeClick, isCancelable)

            viewModel.alertDialogLiveEvent.value = null
        }

        Unit
    }

    private val permissionObserver = { pair: Pair<Array<String>, Int>? ->
        if (pair != null) {
            PermissionManager.requestPermissions(this, *pair.first, requestCode = pair.second)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBindingVariable()

        //viewModel.updateLanguage(this)

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
            val bundle = com.merseyside.kmpMerseyLib.utils.Bundle()

            (viewModel as ParcelableViewModel).writeTo(bundle)
            outState.putSerialize(BaseVMFragment.INSTANCE_STATE_KEY, bundle.map, MapSerializer(String.serializer(), String.serializer()))
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            messageLiveEvent.addObserver(messageObserver)
            isInProgressLiveData.addObserver(loadingObserver)
            alertDialogLiveEvent.addObserver(alertDialogModel)
            grantPermissionLiveEvent.addObserver(permissionObserver)
        }


    }

    override fun handleError(throwable: Throwable) {
        viewModel.onError(throwable)
    }

    override fun updateLanguage(context: Context) {
        //viewModel.updateLanguage(context)
    }

    protected abstract fun loadingObserver(isLoading: Boolean)

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
}