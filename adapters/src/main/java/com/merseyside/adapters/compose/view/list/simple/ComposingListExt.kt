package com.merseyside.adapters.compose.view.list.simple

import com.merseyside.adapters.compose.view.base.SCV
import com.merseyside.adapters.config.AdapterConfig
import com.merseyside.adapters.model.VM

fun ListConfig.adapterConfig(configure: AdapterConfig<SCV, VM<SCV>>.() -> Unit) {
    adapterConfig = configure
}