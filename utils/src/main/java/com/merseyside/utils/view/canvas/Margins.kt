package com.merseyside.utils.view.canvas

data class Margins(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    constructor(
        left: Number,
        top: Number,
        right: Number,
        bottom: Number
    ): this(
        left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat()
    )

    constructor(margin: Number): this(
        margin, margin, margin, margin
    )

    constructor(horizontal: Number, vertical: Number): this(
        horizontal, vertical, horizontal, vertical
    )

    companion object {
        fun getEmpty() = Margins(0, 0, 0, 0)
    }
}
