@file:Suppress("UNCHECKED_CAST")

package com.merseyside.utils.time

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

object Conversions {
    const val MILLIS_CONST = 1000L
    const val SECONDS_MINUTES_CONST = 60L
    const val HOURS_CONST = 24L
    const val WEEK_CONST = 7L
}

operator fun <T : TimeUnit> T.plus(increment: Number): T {
    return this + newInstance(increment.toLong())
}

operator fun <T : TimeUnit> T.div(divider: Number): T {
    return this / newInstance(divider.toLong())
}

operator fun <T : TimeUnit> T.times(times: Number): T {
    return this * newInstance(times.toLong())
}

operator fun <T : TimeUnit> T.minus(unary: Number): T {
    return this - newInstance(unary.toLong())
}

operator fun <T : TimeUnit> T.plus(increment: TimeUnit): T {
    return newInstanceMillis(this.millis + increment.millis) as T
}

operator fun <T : TimeUnit> T.div(divider: TimeUnit): T {
    return newInstanceMillis(this.millis / divider.millis) as T
}

operator fun <T : TimeUnit> T.times(times: TimeUnit): T {
    return newInstanceMillis(this.millis * times.millis) as T
}

operator fun <T : TimeUnit> T.minus(unary: TimeUnit): T {
    return newInstanceMillis(this.millis - unary.millis) as T
}

operator fun <T : TimeUnit> T.inc(): T {
    return this + newInstance(1)
}

operator fun <T : TimeUnit> T.dec(): T {
    return this - newInstance(1)
}

fun <T : TimeUnit> T.isEqual(other: T): Boolean {
    return this.millis == other.millis
}

operator fun <T : TimeUnit> T.compareTo(value: Number): Int {
    return this.millis.compareTo(newInstanceMillis(value.toLong()).millis)
}

fun <T : TimeUnit> T.isNotEqual(other: T) = !isEqual(other)

fun <T : TimeUnit> T.round() = newInstance(value)

interface TimeUnit : Comparable<TimeUnit> {

    val millis: Long
    val value: Long

    fun toMillis(): Millis {
        return Millis(millis)
    }

    fun toSeconds(): Seconds {
        return Seconds(this)
    }

    fun toMinutes(): Minutes {
        return Minutes(this)
    }

    fun toHours(): Hours {
        return Hours(this)
    }

    fun toDays(): Days {
        return Days(this)
    }

    fun toWeeks(): Weeks {
        return Weeks(this)
    }

//    fun getDayOfYear(context: Context? = null): Days {
//        return Days(getFormattedDate(this, "DD", context))
//    }

    fun newInstance(value: Long): TimeUnit

    fun newInstanceMillis(millis: Long): TimeUnit

    override fun toString(): String

    /**
     * Returns true if value == 0
     */
    fun isEmpty(): Boolean {
        return millis <= 0L
    }

    fun isNotEmpty() = !isEmpty()

    override fun compareTo(other: TimeUnit): Int {
        return this.millis.compareTo(other.millis)
    }

    fun getString(): String {
        return "${this::class.simpleName} = $value Millis = $millis"
    }

    companion object {
        fun getEmpty(): TimeUnit {
            return Millis(0)
        }
    }
}

@Serializable
@JvmInline
value class Millis(override val millis: Long) : TimeUnit {

    override val value: Long
        get() = millis


    internal constructor(unit: TimeUnit) : this(unit.millis)

    constructor(number: Number) : this(number.toLong())
    constructor(str: String) : this(str.toLong())
    constructor() : this(0)

    override fun newInstance(value: Long): TimeUnit {
        return Millis(value)
    }

    override fun newInstanceMillis(millis: Long): Millis {
        return Millis(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

@Serializable(with = LongAsSecondsSerializer::class)
@JvmInline
value class Seconds private constructor(override val millis: Long) : TimeUnit {

    override val value: Long
        get() = millis / Conversions.MILLIS_CONST

    internal constructor(unit: TimeUnit) : this(unit.millis)

    constructor(number: Number) : this(
        (Millis(Conversions.MILLIS_CONST) * number).millis
    )

    constructor(str: String) : this(number = str.toLong())
    constructor() : this(0)

    override fun newInstance(value: Long): Seconds {
        return Seconds(Millis(Conversions.MILLIS_CONST).millis * value)
    }

    override fun newInstanceMillis(millis: Long): Seconds {
        return Seconds(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

@Serializable(with = LongAsMinutesSerializer::class)
@JvmInline
value class Minutes private constructor(override val millis: Long) : TimeUnit {

    override val value: Long
        get() = toSeconds().value / Conversions.SECONDS_MINUTES_CONST

    internal constructor(unit: TimeUnit) : this(unit.millis)

    constructor(number: Number) : this(
        (Seconds(Conversions.SECONDS_MINUTES_CONST).millis * number.toLong())
    )

    constructor(str: String) : this(number = str.toLong())
    constructor() : this(0)

    override fun newInstance(value: Long): TimeUnit {
        return Minutes(Seconds(Conversions.SECONDS_MINUTES_CONST).millis * value)
    }

    override fun newInstanceMillis(millis: Long): Minutes {
        return Minutes(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

@Serializable(with = LongAsHoursSerializer::class)
@JvmInline
value class Hours private constructor(override val millis: Long) : TimeUnit {

    internal constructor(unit: TimeUnit) : this(unit.millis)

    constructor(number: Number) : this(
        (Minutes(Conversions.SECONDS_MINUTES_CONST).millis * number.toLong())
    )

    constructor(str: String) : this(number = str.toLong())
    constructor() : this(0)

    override val value: Long
        get() = toMinutes().value / Conversions.SECONDS_MINUTES_CONST

    override fun newInstance(value: Long): TimeUnit {
        return Hours(Minutes(Conversions.SECONDS_MINUTES_CONST).millis * value)
    }

    override fun newInstanceMillis(millis: Long): Hours {
        return Hours(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

@Serializable(with = LongAsDaysSerializer::class)
@JvmInline
value class Days private constructor(override val millis: Long) : TimeUnit {

    internal constructor(unit: TimeUnit) : this(unit.millis)

    constructor(number: Number) : this(
        (Hours(Conversions.HOURS_CONST).millis * number.toLong())
    )

    constructor(str: String) : this(number = str.toLong())
    constructor() : this(0)

    override val value: Long
        get() = toHours().value / Conversions.HOURS_CONST

    override fun newInstance(value: Long): TimeUnit {
        return Days(Hours(Conversions.HOURS_CONST).millis * value)
    }

    override fun newInstanceMillis(millis: Long): Days {
        return Days(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

@Serializable(with = LongAsWeeksSerializer::class)
@JvmInline
value class Weeks private constructor(override val millis: Long) : TimeUnit {

    internal constructor(unit: TimeUnit) : this(unit.millis)

    constructor(number: Number) : this(
        (Days(Conversions.WEEK_CONST).millis * number.toLong())
    )

    constructor(str: String) : this(number = str.toLong())
    constructor() : this(0)

    override val value: Long
        get() = toDays().value / Conversions.WEEK_CONST

    override fun newInstance(value: Long): TimeUnit {
        return Weeks(Days(Conversions.WEEK_CONST).millis * value)
    }

    override fun newInstanceMillis(millis: Long): Weeks {
        return Weeks(millis)
    }

    override fun toString(): String {
        return getString()
    }
}