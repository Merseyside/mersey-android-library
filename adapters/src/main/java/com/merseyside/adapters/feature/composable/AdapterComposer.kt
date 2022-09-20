package com.merseyside.adapters.feature.composable

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.merseyside.adapters.feature.composable.adapter.ViewCompositeAdapter

abstract class AdapterComposer(
    override val adapter: ViewCompositeAdapter,
    val viewLifecleOwner: LifecycleOwner
): HasComposableAdapter {

    protected fun addDataSource(ld: LiveData<*>) {
        ld.observe(viewLifecleOwner) { invalidateAsync() }
    }
}