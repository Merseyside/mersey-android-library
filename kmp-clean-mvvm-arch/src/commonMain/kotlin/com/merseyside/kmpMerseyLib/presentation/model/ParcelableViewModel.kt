package com.merseyside.kmpMerseyLib.presentation.model

import com.merseyside.kmpMerseyLib.utils.Bundle


abstract class ParcelableViewModel: BaseViewModel() {

    abstract fun readFrom(bundle: Bundle)

    abstract fun writeTo(bundle: Bundle)
}