package com.merseyside.adapters.feature.compositeScreen.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//fun <Data> ViewBuilder<Data>.setDataFlow(
//    flow: Flow<Data>,
//    lifecycleScope: LifecycleCoroutineScope
//) {
//    lifecycleScope.launch {
//        repeatOnLifecycle(Lifecycle.State.STARTED) {
//            flow.collect { data ->
//                setData(data)
//            }
//        }
//    }
//}