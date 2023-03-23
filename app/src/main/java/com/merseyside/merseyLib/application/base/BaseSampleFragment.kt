package com.merseyside.merseyLib.application.base

import androidx.databinding.ViewDataBinding
import com.merseyside.archy.presentation.fragment.BaseVMFragment
import com.merseyside.archy.presentation.model.BaseViewModel
import com.merseyside.merseyLib.application.SampleApplication

abstract class BaseSampleFragment<V: ViewDataBinding, M: BaseViewModel> : BaseVMFragment<V, M>() {

    protected val appComponent = SampleApplication.getInstance().appComponent

    abstract fun hasTitleBackButton(): Boolean

    override fun onStart() {
        super.onStart()
        setTitleBackButtonEnabled()
    }

    private fun setTitleBackButtonEnabled() {
        if (getActionBar() != null) {
            getActionBar()!!.setDisplayHomeAsUpEnabled(hasTitleBackButton())

            if (hasTitleBackButton()) {
                setHasOptionsMenu(true)
            }
        }
    }

//    @CallSuper
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//
//        if (id == android.R.id.home) {
//            goBack()
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
}