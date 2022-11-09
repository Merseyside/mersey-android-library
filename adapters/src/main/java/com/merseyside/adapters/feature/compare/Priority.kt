package com.merseyside.adapters.feature.compare

object Priority {

    fun validatePriority(priority: Int) {
        if (priority !in ALWAYS_FIRST_PRIORITY..ALWAYS_LAST_PRIORITY) {
            throw IllegalArgumentException("Wrong priority!")
        }
    }

    const val ALWAYS_FIRST_PRIORITY = -1
    const val ALWAYS_LAST_PRIORITY = Int.MAX_VALUE
}