package com.merseyside.merseyLib.utils.billing

sealed class Period {

    abstract val count: String
    abstract val unit: String

    fun getHumanReadablePeriod(): String {
        return "$count $unit"
    }

    object ThreeDays : Period() {
        override val count: String
            get() = "3"
        override val unit: String
            get() = "Days"

    }

    object OneWeek : Period() {
        override val count: String
            get() = "1"
        override val unit: String
            get() = "Week"

    }

    object FourteenDays : Period() {
        override val count: String
            get() = "14"
        override val unit: String
            get() = "Days"

    }

    object FourWeeks : Period() {
        override val count: String
            get() = "4"
        override val unit: String
            get() = "Weeks"

    }

    object OneMonth : Period() {
        override val count: String
            get() = "1"
        override val unit: String
            get() = "Month"

    }

    object ThreeMonth : Period() {
        override val count: String
            get() = "3"
        override val unit: String
            get() = "Month"

    }

    object SixMonth : Period() {
        override val count: String
            get() = "6"
        override val unit: String
            get() = "Month"

    }

    object OneYear : Period() {
        override val count: String
            get() = "1"
        override val unit: String
            get() = "Year"

    }

    companion object {
        fun stringToPeriod(period: String): Period {
            return when(period) {
                "P3D" -> {
                    ThreeDays
                }

                "P1W" -> {
                    OneWeek
                }

                "P14D" -> {
                    FourteenDays
                }

                "P4W" -> {
                    FourWeeks
                }

                "P1M" -> {
                    OneMonth
                }

                "P3M" -> {
                    ThreeMonth
                }

                "P6M" -> {
                    SixMonth
                }

                "P1Y" -> {
                    OneYear
                }

                else -> throw IllegalArgumentException()
            }
        }
    }
}