package com.merseyside.utils.view

class ViewBaseline {

    companion object {

        fun getHorizontalBaseline(baseline: Int): Int {
            val modes = getModes(baseline)

            return when {
                isHorizontal(modes.first) -> modes.first
                isHorizontal(modes.second) -> modes.second
                else -> UNSPECIFIED
            }.let {
                if (it == CENTER) HORIZONTAL_CENTER
                else it
            }
        }

        fun getVerticalBaseline(baseline: Int): Int {
            val modes = getModes(baseline)

            return when {
                isVertical(modes.first) -> modes.first
                isVertical(modes.second) -> modes.second
                else -> UNSPECIFIED
            }.let {
                if (it == CENTER) VERTICAL_CENTER
                else it
            }
        }

        fun isHorizontal(part: Int): Boolean {
            return when(part) {
                CENTER, HORIZONTAL_CENTER, HORIZONTAL_START, HORIZONTAL_END -> true
                else -> false
            }
        }

        fun isVertical(part: Int): Boolean {
            return when(part) {
                CENTER, VERTICAL_CENTER, VERTICAL_TOP, VERTICAL_BOTTOM -> true
                else -> false
            }
        }

        private fun getModes(baseline: Int): Pair<Int, Int> {
            return (baseline and MASK.inv()) to (baseline and MASK)
        }

        const val UNSPECIFIED = 0
        const val CENTER = 16

        const val VERTICAL_TOP = 1
        const val VERTICAL_BOTTOM = 2
        const val VERTICAL_CENTER = 3

        const val HORIZONTAL_START = 4
        const val HORIZONTAL_END = 8
        const val HORIZONTAL_CENTER = 12

        private const val MASK = 16
    }
}