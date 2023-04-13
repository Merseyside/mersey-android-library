package com.merseyside.utils.timeMeasurer

import com.merseyside.merseyLib.kotlin.logger.log
import com.merseyside.merseyLib.time.units.TimeUnit

fun measureTime(block: () -> Unit): TimeUnit {
    val timeMeasurer = TimeMeasurer()
    timeMeasurer.start()
    block()
    return timeMeasurer.stop()
}

fun <Result> measureAndLogTime(tag: String = "Measuring", block: () -> Result): Result {
    var result: Result? = null
    measureTime {
         result = block()
    }.log(tag, "measured time =")
    return result!!
}