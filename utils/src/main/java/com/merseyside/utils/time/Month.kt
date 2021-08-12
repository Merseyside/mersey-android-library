package com.merseyside.utils.time

enum class Month(val index: Int, val days: Days) {

    JANUARY(0, 31), FEBRUARY(1, 28), MARCH(2, 31),
    APRIL(3, 30), MAY(4, 31), JUNE(5, 30),
    JULY(6, 31), AUGUST(7, 31), SEPTEMBER(8, 30),
    OCTOBER(9, 31), NOVEMBER(10, 30), DECEMBER(11, 31);

    constructor(index: Int, dayCount: Int): this(index, Days(dayCount))

    companion object {
        fun getByIndex(index: Int): Month {
            return Month.values().find { it.index == index }
                ?: throw IllegalArgumentException("Index must be in 0..6 range")
        }
    }
}