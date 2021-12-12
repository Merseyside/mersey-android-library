package com.merseyside.merseyLib.features.adapters.racers.entity

import com.merseyside.merseyLib.kotlin.extensions.forEachEntry
import com.merseyside.merseyLib.kotlin.extensions.log
import com.merseyside.merseyLib.kotlin.extensions.minByNullable
import com.merseyside.merseyLib.time.Millis
import com.merseyside.merseyLib.time.TimeUnit
import com.merseyside.utils.ext.*
import com.merseyside.merseyLib.time.*

class Statistics(
    racers: List<RacerModel>,
    private val lapCheckpointsAverageTime: List<Float>
) {

    private val racersStats: MutableMap<RacerModel, MutableList<MutableList<TimeUnit>>> = LinkedHashMap()

    init {
        racers.forEach { racersStats[it] = mutableListOf(mutableListOf()) }
    }

    fun setCheckpointTime(racer: RacerModel, time: TimeUnit) {
        if (racersStats.contains(racer)) {
            val lap = getRacerCurrentLapResults(racer)
            lap.add(time)

            if (isNextCheckpointFromNewLap(racer)) {
                startNewLap(racer)
            }

            getCurrentLapNumber(racer).log(racer)
            getCurrentCheckpointNumber(racer).log(racer)
        }
    }

    fun getGap(racer: RacerModel): Millis {
        val leader = getLeader()

        return if (racer == leader) {
            Millis(0)
        } else {
            calculateTime(leader, racer).toMillis()
        }
    }

    fun getNextCheckpointAverageTime(racer: RacerModel): TimeUnit {
        return Millis(lapCheckpointsAverageTime[getCurrentCheckpointNumber(racer)] * 1000)
    }

    private fun getRacerResults(racer: RacerModel): MutableList<MutableList<TimeUnit>> {
        return (racersStats[racer] ?: error(""))
    }

    private fun startNewLap(racer: RacerModel): MutableList<TimeUnit> {
        return emptyList<TimeUnit>().toMutableList().also { racersStats[racer]!!.add(it) }
    }

    private fun getRacerCurrentLapResults(racer: RacerModel): MutableList<TimeUnit> {
        return getRacerResults(racer).last()
    }

    private fun getCurrentLapNumber(racer: RacerModel): Int {
        return getRacerResults(racer).size
    }

    private fun getCurrentCheckpointNumber(racer: RacerModel): Int {
        return getRacerCurrentLapResults(racer).size
    }

    private fun getCheckpointsCount(): Int {
        return lapCheckpointsAverageTime.size
    }

    private fun isNextCheckpointFromNewLap(racer: RacerModel): Boolean {
        return getCheckpointsCount() == getCurrentCheckpointNumber(racer)
    }

    private fun getTotalTime(racer: RacerModel): TimeUnit {
        return getRacerResults(racer).flatten().sum()
    }

    private fun getLeader(): RacerModel {
        val possibleLeaders = ArrayList<RacerModel>()

        racersStats.forEachEntry { newRacer, _ ->
            if (getTotalTime(newRacer).isNotEmpty()) {
                if (possibleLeaders.isEmpty()) {
                    possibleLeaders.add(newRacer)
                } else {
                    val oldRacer = possibleLeaders.first()
                    val lapsPassed = getCurrentLapNumber(oldRacer)
                    val checkpointsPassed = getCurrentCheckpointNumber(oldRacer)

                    val lapsPassedNew = getCurrentLapNumber(newRacer)
                    val checkpointsPassedNew = getCurrentCheckpointNumber(newRacer)

                    if (lapsPassed < lapsPassedNew ||
                        (lapsPassed == lapsPassedNew && checkpointsPassed < checkpointsPassedNew)
                    ) {
                        possibleLeaders.clear()
                        possibleLeaders.add(newRacer)
                    } else if (lapsPassed == lapsPassedNew && checkpointsPassed == checkpointsPassedNew) {
                        possibleLeaders.add(newRacer)
                    }
                }
            }
        }

        return if (possibleLeaders.size > 1) {
            possibleLeaders.minByNullable { getTotalTime(it) } ?: throw IllegalArgumentException()
        } else {
            possibleLeaders.first()
        }
    }

    private fun calculateTime(leader: RacerModel, racer: RacerModel): TimeUnit {
        val currentLeaderLap = getCurrentLapNumber(leader)
        val currentLeaderCheckpoint = getCurrentCheckpointNumber(leader)

        val currentLap = getCurrentLapNumber(racer)
        val currentCheckpoint = getCurrentCheckpointNumber(racer)

        return if (currentLap == currentLeaderLap &&
            currentCheckpoint == currentLeaderCheckpoint
        ) {
            getTotalTime(racer) - getTotalTime(leader)
        } else {
            val checkpointCount = getCheckpointsCount()

            val timePoints = getRacerResults(leader).flatten()

            var notPassedSum = 0F
            for (i in (checkpointCount * (currentLap-1)) + currentCheckpoint + 1 until timePoints.size) {
                notPassedSum += timePoints[i].millis.toFloat()
            }

            var leaderMirrorSum = 0F
            for (i in 0 until (checkpointCount * (currentLap-1)) + currentCheckpoint) {
                leaderMirrorSum += timePoints[i].millis.toFloat()
            }

            getTotalTime(racer) - Millis(leaderMirrorSum) + Millis(notPassedSum)
        }
    }
}