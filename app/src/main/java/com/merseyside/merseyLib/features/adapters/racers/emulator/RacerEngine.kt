package com.merseyside.merseyLib.features.adapters.racers.emulator

import com.merseyside.merseyLib.features.adapters.racers.entity.RacerModel
import com.merseyside.merseyLib.time.Millis
import com.merseyside.merseyLib.time.TimeUnit
import com.merseyside.utils.ext.delay
import com.merseyside.utils.time.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class RacerEngine(
    private val racer: RacerModel,
    private val callback: RacerCallback,
    private val scope: CoroutineScope,
) {

    private var job: Job? = null

    private var racerCheckpointTime: TimeUnit = Millis()
    var time: TimeUnit = Millis()

    fun start() {
        if (job?.isActive == true) return

        if (racerCheckpointTime.isEmpty()) {
            val firstCheckpoint = callback.getNextCheckpointTime(racer)
            racerCheckpointTime = Millis(Random.nextDouble(0.0, firstCheckpoint.millis.toDouble()))
        }

        job = scope.launch {
            while(isActive) {
                if (time >= racerCheckpointTime) {
                    time = Millis()

                    callback.onCheckpoint(racer, racerCheckpointTime)

                    val newCheckpointAverageTime = callback.getNextCheckpointTime(racer)
                    racerCheckpointTime = calculateCheckpointTime(newCheckpointAverageTime)
                }
                delay(TIME_DELAY)

                time += TIME_DELAY
            }
        }
    }

    fun pause() {
        job?.cancel()
        job = null
    }

    fun stop() {
        racerCheckpointTime = Millis()
        time = Millis()
    }

    private fun calculateCheckpointTime(checkpointTime: TimeUnit): TimeUnit {
        val delta = checkpointTime.millis.toDouble() * 0.05
        return checkpointTime + Millis((Random.nextDouble(from = -delta, until = delta)))
    }

    interface RacerCallback {
        suspend fun onCheckpoint(racer: RacerModel, checkpointTime: TimeUnit)

        fun getNextCheckpointTime(racer: RacerModel): TimeUnit
    }

    companion object {
        val TIME_DELAY = Millis(5)
    }
}