@file:Suppress("UNCHECKED_CAST")

package com.merseyside.kmpMerseyLib.utils.time

object Conversions {
    const val MILLIS_CONST = 1000L
    const val SECONDS_MINUTES_CONST = 60L
    const val HOURS_CONST = 24L
}

operator fun <T: TimeUnit> T.plus(increment: Long): T {
    return newInstance(value + increment) as T
}

operator fun <T: TimeUnit> T.div(divider: Long): T {
    return newInstance(value / divider) as T
}

operator fun <T: TimeUnit> T.times(times: Long): T {
    return newInstance(value * times) as T
}

operator fun <T: TimeUnit> T.minus(unary: Long): T {
    return newInstance(value - unary) as T
}

operator fun <T: TimeUnit> T.plus(increment: TimeUnit): T {
    return this + convert(increment)
}

operator fun <T: TimeUnit> T.div(divider: TimeUnit): T {
    return this / convert(divider)
}

operator fun <T: TimeUnit> T.times(times: TimeUnit): T {
    return this * convert(times)
}

operator fun <T: TimeUnit> T.minus(unary: TimeUnit): T {
    return this - convert(unary)
}

operator fun <T: TimeUnit> T.compareTo(other: T): Int {
    return this.toMillisLong().compareTo(other.toMillisLong())
}

fun <T: TimeUnit> T.isEqual(other: T): Boolean {
    return this.toMillisLong() == other.toMillisLong()
}

fun <T: TimeUnit> T.isNotEqual(other: T) = !isEqual(other)

interface TimeUnit {

    val value: Long

    fun toMillisLong(): Long {
        return toMillis().value
    }

    fun toMillis(): Millis
    fun toSeconds(): Seconds
    fun toMinutes(): Minutes
    fun toHours(): Hours
    fun toDays(): Days

//    fun getDayOfYear(context: Context? = null): Days {
//        return Days(getFormattedDate(this, "DD", context))
//    }

    fun newInstance(value: Long): TimeUnit

    fun toLong(): Long {
        return value
    }

    override fun toString(): String

    /**
     * Returns true if value <= 0
     */
    fun isEmpty(): Boolean {
        return value <= 0
    }

    fun isNotEmpty() = !isEmpty()

    fun convert(convertingUnit: TimeUnit): Long {

        val value = when (this) {
            is Millis -> convertingUnit.toMillis()
            is Seconds -> convertingUnit.toSeconds()
            is Minutes -> convertingUnit.toMinutes()
            is Hours -> convertingUnit.toHours()
            is Days -> convertingUnit.toDays()
            else -> throw IllegalArgumentException("Wrong type")
        }

        return value.toLong()
    }
}

inline class Millis(override val value: Long): TimeUnit {

    internal constructor(unit: TimeUnit): this(unit.value)
    constructor(string: String): this(string.toLong())

    override fun toMillis(): Millis {
        return this
    }

    override fun toSeconds(): Seconds {
        return Seconds(value / Conversions.MILLIS_CONST)
    }

    override fun toMinutes(): Minutes {
        return Minutes(toSeconds() / Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toHours(): Hours {
        return Hours(toMinutes() / Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toDays(): Days {
        return Days(toHours() / Conversions.HOURS_CONST)
    }

    override fun newInstance(value: Long): Millis {
        return Millis(value)
    }

    override fun toString(): String {
        return value.toString()
    }
}

inline class Seconds(override val value: Long): TimeUnit {

    internal constructor(unit: TimeUnit): this(unit.value)
    constructor(string: String): this(string.toLong())

    override fun toMillis(): Millis {
        return Millis(value * Conversions.MILLIS_CONST)
    }

    override fun toSeconds(): Seconds {
        return this
    }

    override fun toMinutes(): Minutes {
        return Minutes(value / Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toHours(): Hours {
        return Hours(toMinutes() / Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toDays(): Days {
        return Days(toHours() / Conversions.HOURS_CONST)
    }

    override fun newInstance(value: Long): Seconds {
        return Seconds(value)
    }

    override fun toString(): String {
        return value.toString()
    }
}

inline class Minutes(override val value: Long): TimeUnit {

    internal constructor(unit: TimeUnit): this(unit.value)
    constructor(string: String): this(string.toLong())

    override fun toMillis(): Millis {
        return Millis(toSeconds() * Conversions.MILLIS_CONST)
    }

    override fun toSeconds(): Seconds {
        return Seconds(value * Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toMinutes(): Minutes {
        return this
    }

    override fun toHours(): Hours {
        return Hours(value / Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toDays(): Days {
        return Days(toHours() / Conversions.HOURS_CONST)
    }

    override fun newInstance(value: Long): Minutes {
        return Minutes(value)
    }

    override fun toString(): String {
        return value.toString()
    }
}

inline class Hours(override val value: Long): TimeUnit {

    internal constructor(unit: TimeUnit): this(unit.value)
    constructor(string: String): this(string.toLong())

    override fun toMillis(): Millis {
        return Millis(toSeconds() * Conversions.MILLIS_CONST)
    }

    override fun toSeconds(): Seconds {
        return Seconds(toMinutes() * Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toMinutes(): Minutes {
        return Minutes(value * Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toHours(): Hours {
        return this
    }

    override fun toDays(): Days {
        return Days(value / Conversions.HOURS_CONST)
    }

    override fun newInstance(value: Long): Hours {
        return Hours(value)
    }

    override fun toString(): String {
        return value.toString()
    }
}

inline class Days(override val value: Long): TimeUnit {

    internal constructor(unit: TimeUnit): this(unit.value)
    constructor(string: String): this(string.toLong())

    override fun toMillis(): Millis {
        return Millis(toSeconds() * Conversions.MILLIS_CONST)
    }

    override fun toSeconds(): Seconds {
        return Seconds(toMinutes() * Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toMinutes(): Minutes {
        return Minutes(toHours() * Conversions.SECONDS_MINUTES_CONST)
    }

    override fun toHours(): Hours {
        return Hours(value * Conversions.HOURS_CONST)
    }

    override fun toDays(): Days {
        return this
    }

    override fun newInstance(value: Long): Days {
        return Days(value)
    }

    override fun toString(): String {
        return value.toString()
    }
}