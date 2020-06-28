package com.merseyside.merseyLib.application.main

import android.content.Context
import androidx.databinding.ObservableField
import com.merseyside.archy.model.BaseViewModel
import com.merseyside.archy.view.localeViews.LocaleData

class MainViewModel : BaseViewModel() {

    val stringObservableField = ObservableField<LocaleData>()
    val isVisible = ObservableField(true)

    override fun dispose() {
    }

    override fun getLocaleContext(): Context {
        TODO("implementation")
    }
}