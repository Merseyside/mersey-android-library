package com.merseyside.archy.presentation.model

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.merseyside.archy.presentation.model.ParcelableViewModel

abstract class BundleAwareViewModelFactory<T: ParcelableViewModel> (
    private val bundle: Bundle? = null
) : BaseViewModelFactory<T>() {

    override fun <M: ViewModel?> create(modelClass: Class<M>): M {

        val viewModel = getViewModel()

        if (bundle != null && !bundle.isEmpty) {
            viewModel.readFrom(bundle)
        }

        return viewModel as M
    }


}