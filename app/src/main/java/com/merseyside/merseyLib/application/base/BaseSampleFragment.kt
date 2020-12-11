package com.merseyside.merseyLib.application.base

import androidx.databinding.ViewDataBinding
import com.merseyside.archy.presentation.fragment.BaseVMFragment
import com.merseyside.archy.presentation.model.BaseViewModel
import com.merseyside.merseyLib.application.SampleApplication

abstract class BaseSampleFragment<V: ViewDataBinding, M: BaseViewModel> : BaseVMFragment<V, M>() {

    protected val appComponent = SampleApplication.getInstance().appComponent
}