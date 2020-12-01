package com.merseyside.archy.presentation.model

import android.app.Application
import android.content.Context

abstract class AndroidViewModel(val application: Application): BaseViewModel() {

    override fun getLocaleContext(): Context {
        return application
    }
}