package com.merseyside.archy.presentation.model

import android.app.Application
import android.os.Bundle

abstract class ParcelableViewModel(application: Application): AndroidViewModel(application) {

    abstract fun readFrom(bundle: Bundle)

    abstract fun writeTo(bundle: Bundle)
}