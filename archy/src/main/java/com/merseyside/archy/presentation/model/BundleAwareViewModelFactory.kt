package com.merseyside.archy.presentation.model

import android.os.Bundle
import androidx.lifecycle.ViewModel

abstract class BundleAwareViewModelFactory<T: ParcelableViewModel> (
    private val bundle: Bundle? = null
) : BaseViewModelFactory<T>() {

    @Suppress("UNCHECKED_CAST")
    override fun <M: ViewModel?> create(modelClass: Class<M>): M {
        val viewModel = getViewModel()

        if (bundle != null && !bundle.isEmpty) {
            viewModel.readFrom(bundle)
        }

        return viewModel as M
    }
}