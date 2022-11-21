package com.merseyside.adapters.feature.selecting.ext

import com.merseyside.adapters.feature.selecting.AdapterSelect

fun AdapterSelect<*, *>.clearAsync(onComplete: (Unit) -> Unit = {}) {
    workManager.doAsync(onComplete) { clear() }
}