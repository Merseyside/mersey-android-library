package com.merseyside.adapters.compose.utils

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