package com.merseyside.utils.math

import kotlin.math.ceil
import kotlin.math.floor

/**
 * Rounds the given value [x] to an integer towards positive infinity.

 * @return the smallest Float value that is greater than or equal to the given value [x] and is a mathematical integer.
 *
 * Special cases:
 *   - `ceil(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer.
 */
fun ceilInt(float: Float): Int {
    return ceil(float).toInt()
}

/**
 * Rounds the given value [x] to an integer towards positive infinity.

 * @return the smallest Float value that is greater than or equal to the given value [x] and is a mathematical integer.
 *
 * Special cases:
 *   - `ceil(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer.
 */
fun ceilInt(double: Double): Int {
    return ceil(double).toInt()
}

/**
 * Rounds the given value [x] to an integer towards negative infinity.

 * @return the largest Float value that is smaller than or equal to the given value [x] and is a mathematical integer.
 *
 * Special cases:
 *   - `floor(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer.
 */
fun floorInt(float: Float): Int {
    return floor(float).toInt()
}

/**
 * Rounds the given value [x] to an integer towards negative infinity.

 * @return the largest Float value that is smaller than or equal to the given value [x] and is a mathematical integer.
 *
 * Special cases:
 *   - `floor(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer.
 */
fun floorInt(double: Double): Int {
    return floor(double).toInt()
}