package com.merseyside.utils.time

enum class DayOfWeek(val index: Int) {
    MONDAY(0), TUESDAY(1), WEDNESDAY(2), THURSDAY(3), FRIDAY(4), SATURDAY(5), SUNDAY(6);

    companion object {
        fun getByIndex(index: Int): DayOfWeek {
            return values().find { it.index == index }
                ?: throw IllegalArgumentException("Index must be in 0..6 range")
        }
    }
}