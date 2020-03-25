package com.merseyside.merseyLib.presentation.model

import android.os.Bundle

abstract class ParcelableViewModel: BaseViewModel() {

    abstract fun readFrom(bundle: Bundle)

    abstract fun writeTo(bundle: Bundle)
}