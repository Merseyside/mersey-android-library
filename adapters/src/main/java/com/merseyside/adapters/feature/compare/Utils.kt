package com.merseyside.adapters.feature.compare


fun validatePriority(priority: Int) {
    if (priority !in Priority.ALWAYS_FIRST_PRIORITY..Priority.ALWAYS_LAST_PRIORITY) {
        throw IllegalArgumentException("Wrong priority!")
    }
}