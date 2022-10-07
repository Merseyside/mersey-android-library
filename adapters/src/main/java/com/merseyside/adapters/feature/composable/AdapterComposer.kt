package com.merseyside.adapters.feature.composable

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.merseyside.adapters.feature.composable.model.ViewAdapterViewModel

abstract class AdapterComposer<Model>(
    val viewLifecleOwner: LifecycleOwner
): HasComposableAdapter<Model>
    where Model : ViewAdapterViewModel {

    protected fun addDataSource(ld: LiveData<*>) {
        ld.observe(viewLifecleOwner) { invalidateAsync() }
    }
}

abstract class SimpleAdapterComposer(
    viewLifecleOwner: LifecycleOwner
): AdapterComposer<ViewAdapterViewModel>(viewLifecleOwner), HasSimpleComposableAdapter