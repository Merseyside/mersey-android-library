package com.merseyside.merseyLib.application.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.merseyside.archy.presentation.fragment.BaseVMFragment
import com.merseyside.archy.presentation.model.BaseViewModel
import com.merseyside.archy.utils.ext.navigateUp
import com.merseyside.merseyLib.application.SampleApplication
import com.merseyside.utils.navigation.setupWithNavController

abstract class BaseSampleFragment<V: ViewDataBinding, M: BaseViewModel> : BaseVMFragment<V, M>() {

    protected val appComponent = SampleApplication.getInstance().appComponent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseActivity.getToolbar()?.let {
            setupWithNavController(it) {
                navigateUp()
            }
        }
    }

    override fun isNavigateUpEnabled(): Boolean {
        return true
    }

    override fun onNavigateUp() {
        findNavController().navigateUp()
    }
}