package com.merseyside.merseyLib.features.adapters.colors.model

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.merseyside.archy.presentation.model.AndroidViewModel
import com.merseyside.merseyLib.features.adapters.colors.entity.HexColor
import com.merseyside.merseyLib.features.adapters.colors.producer.ColorProducer
import com.merseyside.merseyLib.time.units.Seconds
import com.merseyside.utils.delayedMainThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ColorsViewModel(
    application: Application,
    private val colorProducer: ColorProducer
) : AndroidViewModel(application) {

    init {
        delayedMainThread(Seconds(1)) {
            repeat(10) { onPopulateClick() }
        }
    }

    val isAddingObservableField = ObservableBoolean(true)
    val isFilterObservableField = ObservableBoolean(false)

    fun getColorsFlow(): Flow<List<HexColor>> = colorProducer.getColorsSharedFlow()

    fun onPopulateClick() {
        viewModelScope.launch {
            colorProducer.generateRandomColors(15)
        }
    }
}