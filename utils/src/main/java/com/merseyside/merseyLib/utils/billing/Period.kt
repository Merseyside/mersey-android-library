package com.merseyside.merseyLib.utils.billing

import com.merseyside.merseyLib.utils.time.TimeUnit

sealed class Period {

    abstract val count: Int
    abstract val unit: String

    fun getHumanReadablePeriod(): String {
        return "$count $unit"
    }

    abstract fun toTimeUnit(): TimeUnit

    abstract class Days(override val count: Int) : Period() {
        override val unit: String
            get() {
                var str = "Day"
                if (count > 1) str += "s"

                return str
            }

        override fun toTimeUnit(): TimeUnit {
            return com.merseyside.merseyLib.utils.time.Days(count)
        }
    }

    abstract class Weeks(override val count: Int) : Period() {
        override val unit: String
            get() {
                var str = "Week"
                if (count > 1) str += "s"

                return str
            }

        override fun toTimeUnit(): TimeUnit {
            return com.merseyside.merseyLib.utils.time.Days(count * 7)
        }
    }

    abstract class Month(override val count: Int) : Period() {
        override val unit: String
            get() {
                return "Month"
            }

        override fun toTimeUnit(): TimeUnit {
            return com.merseyside.merseyLib.utils.time.Days(count * 30)
        }
    }

    abstract class Years(override val count: Int) : Period() {
        override val unit: String
            get() {
                var str = "Year"
                if (count > 1) str += "s"

                return str
            }

        override fun toTimeUnit(): TimeUnit {
            return com.merseyside.merseyLib.utils.time.Days(count * 365)
        }
    }

    object ThreeDays : Days(3)
    object OneWeek : Weeks(1)
    object FourteenDays : Days(14)
    object FourWeeks : Weeks(4)
    object OneMonth : Month(1)
    object ThreeMonth : Month(3)
    object SixMonth : Month(6)
    object OneYear : Years(1)

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