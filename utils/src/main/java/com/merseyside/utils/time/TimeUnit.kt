@file:Suppress("UNCHECKED_CAST")
package com.merseyside.utils.time

import android.content.Context

object Conversions {
    const val MILLIS_CONST = 1000L
    const val SECONDS_MINUTES_CONST = 60L
    const val HOURS_CONST = 24L
}

operator fun <T: TimeUnit> T.plus(increment: Number): T {
    return this + newInstance(increment.toLong())
}

operator fun <T: TimeUnit> T.div(divider: Number): T {
    return this / newInstance(divider.toLong())
}

operator fun <T: TimeUnit> T.times(times: Number): T {
    return this * newInstance(times.toLong())
}

operator fun <T: TimeUnit> T.minus(unary: Number): T {
    return this - newInstance(unary.toLong())
}

operator fun <T: TimeUnit> T.plus(increment: TimeUnit): T {
    return newInstance(this.millis + increment.millis) as T
}

operator fun <T: TimeUnit> T.div(divider: TimeUnit): T {
    return newInstance(this.millis / divider.millis) as T
}

operator fun <T: TimeUnit> T.times(times: TimeUnit): T {
    return newInstance(this.millis * times.millis) as T
}

operator fun <T: TimeUnit> T.minus(unary: TimeUnit): T {
    return newInstance(this.millis - unary.millis) as T
}

fun <T: TimeUnit> T.isEqual(other: T): Boolean {
    return this.millis == other.millis
}

operator fun <T: TimeUnit> T.compareTo(value: Number): Int {
    return this.millis.compareTo(newInstance(value.toLong()).millis)
}

fun <T: TimeUnit> T.isNotEqual(other: T) = !isEqual(other)

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
//
    fun getDayOfYear(context: Context? = null): Days {
        return Days(getFormattedDate(this, "DD", context))
    }

    fun newInstance(millis: Long): TimeUnit

    override fun toString(): String

    /**
     * Returns true if value == 0
     */
    fun isEmpty(): Boolean {
        return millis == 0L
    }

    fun isNotEmpty() = !isEmpty()

    override fun compareTo(other: TimeUnit): Int {
        return this.millis.compareTo(other.millis)
    }

    fun getString(): String {
        return "${javaClass.simpleName} = $value Millis = $millis"
    }

    companion object {
        fun getEmpty(): TimeUnit {
            return Millis(0)
        }
    }
}

inline class Millis(override val millis: Long): TimeUnit {

    override val value: Long
        get() = millis

    internal constructor(unit: TimeUnit): this(unit.millis)

    constructor(number: Number): this(number.toLong())
    constructor(str: String): this(str.toLong())
    constructor(): this(0)
    

    override fun newInstance(millis: Long): Millis {
        return Millis(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

inline class Seconds private constructor(override val millis: Long): TimeUnit {

    override val value: Long
        get() = millis / Conversions.MILLIS_CONST

    internal constructor(unit: TimeUnit): this(unit.millis)

    constructor(number: Number): this(
        (Millis(Conversions.MILLIS_CONST) * number).millis
    )

    constructor(str: String): this(number = str.toLong())
    constructor(): this(0)

    override fun newInstance(millis: Long): Seconds {
        return Seconds(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

inline class Minutes private constructor(override val millis: Long): TimeUnit {

    internal constructor(unit: TimeUnit): this(unit.millis)

    constructor(number: Number): this(
        (Seconds(Conversions.SECONDS_MINUTES_CONST) * number).millis
    )

    constructor(str: String): this(number = str.toLong())
    constructor(): this(0)

    override val value: Long
        get() = toSeconds().value / Conversions.SECONDS_MINUTES_CONST

    override fun newInstance(millis: Long): Minutes {
        return Minutes(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

inline class Hours private constructor(override val millis: Long): TimeUnit {

    internal constructor(unit: TimeUnit): this(unit.millis)

    constructor(number: Number): this(
        (Minutes(Conversions.SECONDS_MINUTES_CONST) * number).millis
    )

    constructor(str: String): this(number = str.toLong())
    constructor(): this(0)

    override val value: Long
        get() = toMinutes().value / Conversions.SECONDS_MINUTES_CONST

    override fun newInstance(millis: Long): Hours {
        return Hours(millis)
    }

    override fun toString(): String {
        return getString()
    }
}

inline class Days private constructor(override val millis: Long): TimeUnit {

    internal constructor(unit: TimeUnit): this(unit.millis)

    constructor(number: Number): this(
        (Hours(Conversions.HOURS_CONST) * number).millis
    )

    constructor(str: String): this(number = str.toLong())
    constructor(): this(0)

    override val value: Long
        get() = toHours().value / Conversions.HOURS_CONST

    override fun newInstance(millis: Long): Days {
        return Days(millis)
    }

    override fun toString(): String {
        return getString()
    }
}