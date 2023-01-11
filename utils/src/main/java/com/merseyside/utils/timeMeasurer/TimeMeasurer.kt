package com.merseyside.utils.timeMeasurer

import com.merseyside.merseyLib.time.Time
import com.merseyside.merseyLib.time.units.TimeUnit
import com.merseyside.merseyLib.time.units.minus

/**
 * Use only for debugging. Measure time between start() and stop() calls
 */
class TimeMeasurer {

    lateinit var startTime: TimeUnit

    fun start(): TimeUnit {
        return Time.nowGMT.also { startTime = it }
    }

    /**
     * @return measured time
     */
    fun stop(): TimeUnit {
        val now = Time.nowGMT
        return now - startTime
    }
}