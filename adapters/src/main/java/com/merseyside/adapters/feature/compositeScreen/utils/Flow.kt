package com.merseyside.adapters.feature.compositeScreen.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import com.merseyside.adapters.feature.compositeScreen.CompositeScreenBuilder
import com.merseyside.adapters.model.AdapterParentViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle

context(Lifecycle)
fun <Parent, Model : AdapterParentViewModel<out Parent, Parent>>
        CompositeScreenBuilder<Parent, Model>.dataFlow(
    flow: Flow<Any>,
    lifecycleScope: LifecycleCoroutineScope
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { data ->
                setData(data)
            }
        }
    }
}