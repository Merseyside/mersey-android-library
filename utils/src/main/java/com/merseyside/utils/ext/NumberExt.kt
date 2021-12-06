package com.merseyside.utils.ext

import java.util.*

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(Locale.US, this).toDouble()
fun Float.round(decimals: Int = 2): Float = "%.${decimals}f".format(Locale.US, this).toFloat()