package com.merseyside.merseyLib.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.merseyside.merseyLib.presentation.model.BaseViewModel

abstract class BaseMvvmDialog<B : ViewDataBinding, M : BaseViewModel> : BaseDialog() {

    private lateinit var binding: B
    private lateinit var viewModel: M

    private val errorObserver = Observer<Throwable> { this.showError(it) }
    private val messageObserver = Observer<BaseViewModel.TextMessage> { this.showMsg(it) }

    override fun onCreate(onSavedInstanceState: Bundle?) {
        performInjection()
        super.onCreate(onSavedInstanceState)
        viewModel = getViewModel()
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(getBindingVariable(), viewModel)
        binding.executePendingBindings()

        viewModel.updateLanguage(context!!)

        viewModel.errorLiveEvent.observe(this, errorObserver)
        viewModel.messageLiveEvent.observe(this, messageObserver)
    }

    abstract fun getBindingVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): M

    private fun showMsg(textMessage: BaseViewModel.TextMessage) {
        if (textMessage.actionMsg.isNullOrEmpty()) {
            showMsg(textMessage.msg)
        } else {
            showMsg(textMessage.msg, textMessage.actionMsg!!, textMessage.listener!!)
        }
    }
}
