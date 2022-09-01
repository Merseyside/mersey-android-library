package com.merseyside.adapters.feature.compositeScreen.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.merseyside.adapters.feature.compositeScreen.CompositeScreenBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

context(Lifecycle)
fun CompositeScreenBuilder.dataFlow(
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