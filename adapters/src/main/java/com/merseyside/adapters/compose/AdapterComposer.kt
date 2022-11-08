package com.merseyside.adapters.compose

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.merseyside.adapters.compose.model.ViewAdapterViewModel
import com.merseyside.adapters.feature.composable.HasComposableAdapter
import com.merseyside.adapters.feature.composable.HasSimpleComposableAdapter

abstract class AdapterComposer<Model>(
    val viewLifecleOwner: LifecycleOwner
): HasComposableAdapter<Model>
    where Model : ViewAdapterViewModel {

    protected fun addDataSource(ld: LiveData<*>) {
        ld.observe(viewLifecleOwner) { invalidateAsync() }
    }
}

abstract class SimpleAdapterComposer(
    viewLifecycleOwner: LifecycleOwner
): AdapterComposer<ViewAdapterViewModel>(viewLifecycleOwner), HasSimpleComposableAdapter