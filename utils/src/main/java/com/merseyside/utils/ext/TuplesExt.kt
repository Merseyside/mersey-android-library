package com.merseyside.utils.ext

import com.merseyside.utils.Quad

fun Quad<Int, Int, Int, Int>.toFloat(): Quad<Float, Float, Float, Float> {
    return Quad(first.toFloat(), second.toFloat(), third.toFloat(), fourth.toFloat())
}