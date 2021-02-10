package com.merseyside.merseyLib.features.adapters.racers.di

import android.app.Application
import android.content.Context
import com.merseyside.archy.presentation.di.qualifiers.ApplicationContext
import com.merseyside.archy.presentation.ext.viewModel
import com.merseyside.filemanager.AssetHelper
import com.merseyside.merseyLib.features.adapters.racers.emulator.RacingEmulator
import com.merseyside.merseyLib.features.adapters.racers.entity.TeamModel
import com.merseyside.merseyLib.features.adapters.racers.model.RacingViewModel
import com.merseyside.merseyLib.features.adapters.racers.view.RacingFragment
import dagger.Module
import dagger.Provides
import kotlinx.serialization.builtins.ListSerializer
import java.io.FileNotFoundException

@Module
class RacingModule(private val fragment: RacingFragment) {

    @Provides
    fun provideNewsViewModel(
        @ApplicationContext application: Application,
        racingEmulator: RacingEmulator
    ) = fragment.viewModel { RacingViewModel(application, racingEmulator) }

    @Provides
    fun provideRacingEmulator(teams: List<TeamModel>) = RacingEmulator(teams)

    @Provides
    fun provideTeams(@ApplicationContext context: Context): List<TeamModel> {
        return AssetHelper.jsonAssetToModel(context, "racers.json", ListSerializer(TeamModel.serializer())) ?:
            throw FileNotFoundException()
    }
}