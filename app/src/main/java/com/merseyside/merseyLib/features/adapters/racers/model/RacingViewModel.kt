package com.merseyside.merseyLib.features.adapters.racers.model

import android.app.Application
import androidx.databinding.ObservableBoolean
import com.merseyside.archy.presentation.model.AndroidViewModel
import com.merseyside.merseyLib.features.adapters.racers.emulator.RacingEmulator
import com.merseyside.merseyLib.features.adapters.racers.entity.Checkpoint
import kotlinx.coroutines.flow.Flow

class RacingViewModel(
    application: Application,
    private val racingEmulator: RacingEmulator
) : AndroidViewModel(application) {

    val isStarted = ObservableBoolean(false)

    fun getCheckpointFlow(): Flow<Checkpoint> {
        return racingEmulator.getCheckpointFlow()
    }

    fun start() {
        isStarted.set(!isStarted.get())

        if (isStarted.get()) {
            racingEmulator.startRacing()
        } else {
            racingEmulator.pauseRacing()
        }
    }
}