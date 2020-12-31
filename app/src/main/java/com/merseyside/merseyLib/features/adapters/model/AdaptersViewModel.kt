package com.merseyside.merseyLib.features.adapters.model

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.merseyside.archy.presentation.model.AndroidViewModel
import com.merseyside.merseyLib.features.adapters.entity.HexColor
import com.merseyside.merseyLib.features.adapters.producer.ColorProducer
import com.merseyside.utils.mvvm.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdaptersViewModel(
    application: Application,
    private val colorProducer: ColorProducer
) : AndroidViewModel(application) {

    val isAddingObservableField = ObservableBoolean(true)
    val isFilterObservableField = ObservableBoolean(false)

    fun getColorsFlow(): Flow<List<HexColor>> = colorProducer.getColorsSharedFlow()

    fun onPopulateClick() {
        viewModelScope.launch {
            colorProducer.generateRandomColors()
        }
    }
}