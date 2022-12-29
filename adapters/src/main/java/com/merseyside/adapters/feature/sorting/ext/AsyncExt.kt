package com.merseyside.adapters.feature.sorting.ext

import com.merseyside.adapters.feature.sorting.Comparator

fun Comparator<*, *>.updateAsync(onComplete: (Unit) -> Unit = {}) {
    workManager.doAsync(onComplete) { update() }
}