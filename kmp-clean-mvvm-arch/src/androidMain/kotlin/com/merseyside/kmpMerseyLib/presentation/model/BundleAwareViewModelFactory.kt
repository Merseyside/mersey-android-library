package com.merseyside.kmpMerseyLib.presentation.model

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.merseyside.kmpMerseyLib.presentation.fragment.BaseVMFragment
import com.merseyside.utils.serialization.getSerialize
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer

abstract class BundleAwareViewModelFactory<T: ParcelableViewModel> (
    private val bundle: Bundle? = null
) : BaseViewModelFactory<T>() {

    override fun <M: ViewModel?> create(modelClass: Class<M>): M {

        val viewModel = getViewModel()

        if (bundle != null && !bundle.isEmpty) {
            val kmpBundle = com.merseyside.kmpMerseyLib.utils.Bundle(bundle.getSerialize(
                BaseVMFragment.INSTANCE_STATE_KEY,
                MapSerializer(String.serializer(), String.serializer())
            )!!.toMutableMap())

            viewModel.readFrom(kmpBundle)
        }

        return viewModel as M
    }

}